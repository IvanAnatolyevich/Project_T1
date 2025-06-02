package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
