package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.model.AccountType;
import ru.yandex.model.ClientStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private AccountType accountType;
    private long balance;
    private ClientStatus clientStatus;

}
