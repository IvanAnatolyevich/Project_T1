package ru.yandex.service;

import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto addTransaction(Transaction transaction);

    void deleteTransaction(Long transactionId);

    List<TransactionDto> getAllTransactions(int size);
    TransactionDto getTransaction(Long id);
}
