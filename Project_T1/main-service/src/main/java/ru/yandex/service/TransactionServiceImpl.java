package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.aop.annotation.Cached;
import ru.yandex.aop.annotation.Metric;
import ru.yandex.model.Account;
import ru.yandex.model.AccountStatus;
import ru.yandex.model.TransactionStatus;
import ru.yandex.model.dto.TransactionAcceptDto;
import ru.yandex.repository.AccountRepository;
import ru.yandex.repository.TransactionRepository;
import ru.yandex.exception.NotFoundException;
import ru.yandex.mapper.TransactionListMapper;
import ru.yandex.mapper.TransactionMapper;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionListMapper transactionListMapper;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.accept}")
    private String acceptTopic;

    @Override
    public void processIncomingTransaction(TransactionDto dto) {
        Account account = accountRepository.findByAccountId(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Счет не найден"));
        if (!account.getStatus().equals(AccountStatus.OPEN)) return;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAmount(dto.getAmount());
        transaction.setStatus(TransactionStatus.REQUESTED);
        transaction.setAccountId(account.getAccountId());
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() + dto.getAmount());
        accountRepository.save(account);

        TransactionAcceptDto acceptDto = new TransactionAcceptDto(
                dto.getClientId(),
                dto.getAccountId(),
                transaction.getTransactionId(),
                transaction.getTimestamp(),
                transaction.getAmount(),
                account.getBalance()
        );
        kafkaTemplate.send(acceptTopic, acceptDto);
    }

    @Override
    public TransactionDto addTransaction(Transaction transaction) {
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    @Metric
    @Cached
    public List<TransactionDto> getAllTransactions(int size) {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new NotFoundException("История транзакций пуста");
        }
        return transactionListMapper.toDto(transactions);
    }

    @Override
    @Metric
    @Cached
    public TransactionDto getTransaction(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return transactionMapper.toDto(transaction.get());

        }
        throw new NotFoundException("Транзакция с таким id не найдена");
    }
}
