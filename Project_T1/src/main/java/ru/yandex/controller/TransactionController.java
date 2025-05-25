package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.aop.annotation.LogDataSourceError;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;
import ru.yandex.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    @LogDataSourceError
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody Transaction transaction) {
        TransactionDto transactionDto = transactionService.addTransaction(transaction);
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    @LogDataSourceError
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping
    @LogDataSourceError
    public ResponseEntity<List<TransactionDto>> getTransactions(@RequestParam int size) {
        List<TransactionDto> transactions = transactionService.getAllTransactions(size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    @LogDataSourceErrorcd
    public void deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }
}
