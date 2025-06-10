package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
    private final WebClient.Builder webClientBuilder;



    @Value("${kafka.topics.accept}")
    private String acceptTopic;

    @Value("${analysis.service.url}")
    private String analysisServiceUrl;

    @Value("${transaction.reject.threshold}")
    private int rejectThreshold;


    @Override
    public void processIncomingTransaction(TransactionDto dto) {
        Account account = accountRepository.findByAccountId(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Счёт не найден"));

        if (!account.getStatus().equals(AccountStatus.OPEN)) {
            return;
        }

        boolean isBlacklisted = checkClientStatus(dto.getClientId().toString(), dto.getAccountId().toString());
        TransactionStatus transactionStatus;
        if (isBlacklisted) {
            account.setStatus(AccountStatus.BLOCKED);
            transactionStatus = TransactionStatus.REJECTED;
            accountRepository.save(account);
        } else {
            transactionStatus = TransactionStatus.ACCECPTED;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAmount(dto.getAmount());
        transaction.setStatus(transactionStatus);
        transaction.setAccountId(account.getAccountId());
        transactionRepository.save(transaction);

        if (transactionStatus == TransactionStatus.ACCECPTED) {
            account.setBalance(account.getBalance() + dto.getAmount());
            accountRepository.save(account);
        }

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

    private boolean checkClientStatus(String clientId, String accountId) {
        String url = analysisServiceUrl + "/api/client-status?clientId=" + clientId + "&accountId=" + accountId;
        String response = webClientBuilder.build()
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response != null && response.contains("BLOCKED");
    }
}
