package ru.yandex.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;

@Component
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionDto transactionDto);
    TransactionDto toDto(Transaction transaction);
}
