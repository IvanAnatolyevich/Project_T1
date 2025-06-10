package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Transaction;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(UUID accountId);
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.accountId IN " +
            "(SELECT a.accountId FROM Account a WHERE a.clientId = :clientId) " +
            "AND t.status = 'REJECTED'")
    long countRejectedByClientId(@Param("clientId") String clientId);


}
