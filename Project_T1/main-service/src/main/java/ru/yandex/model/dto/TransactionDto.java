package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private long amount;
    private LocalDateTime timestamp;
    private UUID accountId;
    private UUID clientId;

}
