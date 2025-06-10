package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.model.TransactionStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResultDto {
    UUID transactionId;
    UUID accountId;
    TransactionStatus status;
}
