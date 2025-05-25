package ru.yandex.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = TransactionMapper.class)
public interface TransactionListMapper {
    List<Transaction> toEntity(List<TransactionDto> transactionDto);
    List<TransactionDto> toDto(List<Transaction> transaction);
}
