package ru.yandex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

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
    private Long id;
    private Long accountId;
    private long amount;
    private LocalDate date;
}
