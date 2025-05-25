package ru.yandex.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Account;
import ru.yandex.model.dto.AccountDto;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);
}
