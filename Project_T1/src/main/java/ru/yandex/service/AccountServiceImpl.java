package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.exception.NotFoundException;
import ru.yandex.mapper.AccountMapper;
import ru.yandex.model.Account;
import ru.yandex.model.dto.AccountDto;
import ru.yandex.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDto getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            return accountMapper.toDto(account.get());
        }
        throw new NotFoundException("Счет с таким id не найден");
    }

    @Override
    public AccountDto createAccount(Account account) {
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        if (accountRepository.findById(id).isPresent()) {
            accountRepository.deleteById(id);
        }
        throw new NotFoundException("Счет с таким id не найден");
    }
}
