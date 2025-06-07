package ru.yandex.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private AccountType type;
    private Long balance;
    @NonNull
    private Long clientId;
    private AccountStatus status;
    private Long frozenAmount;
    private UUID accountId;
}
