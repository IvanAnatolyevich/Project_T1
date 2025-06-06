package ru.yandex.controller;

import jakarta.validation.Valid;
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
@LogDataSourceError
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody @Valid Transaction transaction) {
        TransactionDto transactionDto = transactionService.addTransaction(transaction);
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getTransactions(@RequestParam int size) {
        List<TransactionDto> transactions = transactionService.getAllTransactions(size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }
}
