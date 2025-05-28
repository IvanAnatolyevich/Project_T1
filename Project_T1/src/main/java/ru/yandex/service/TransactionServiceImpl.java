package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.aop.annotation.Cached;
import ru.yandex.aop.annotation.Metric;
import ru.yandex.repository.TransactionRepository;
import ru.yandex.exception.NotFoundException;
import ru.yandex.mapper.TransactionListMapper;
import ru.yandex.mapper.TransactionMapper;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionListMapper transactionListMapper;
    private final TransactionMapper transactionMapper;


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
