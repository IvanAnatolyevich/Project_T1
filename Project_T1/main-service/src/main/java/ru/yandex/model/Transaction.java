package ru.yandex.model;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private UUID accountId;
    private long amount;
    private LocalDateTime timestamp = LocalDateTime.now();
    private TransactionStatus status;
    private UUID transactionId;
    private UUID clientId;
}
