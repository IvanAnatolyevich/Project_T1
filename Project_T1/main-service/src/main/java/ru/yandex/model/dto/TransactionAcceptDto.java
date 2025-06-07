package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAcceptDto {
    UUID clientId;
    UUID accountId;
    UUID transactionId;
    LocalDateTime timestamp;
    Long amount;
    Long balance;
}
