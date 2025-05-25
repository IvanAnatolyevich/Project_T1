package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
