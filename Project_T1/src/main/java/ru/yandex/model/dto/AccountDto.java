package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.model.AccountType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private AccountType accountType;
    private long balance;
}
