package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.aop.annotation.LogDataSourceError;
import ru.yandex.model.Account;
import ru.yandex.model.dto.AccountDto;
import ru.yandex.service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@LogDataSourceError
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
    }
}

