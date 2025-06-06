package ru.yandex.service;


import ru.yandex.model.Account;
import ru.yandex.model.dto.AccountDto;

public interface AccountService {

    AccountDto getAccountById(Long accountId);

    AccountDto createAccount(Account account);

    void deleteAccount(Long id);

}
