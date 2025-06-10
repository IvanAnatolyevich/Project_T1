package ru.yandex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.yandex.model.Account;
import ru.yandex.model.AccountType;
import ru.yandex.model.Client;
import ru.yandex.model.Transaction;
import ru.yandex.repository.AccountRepository;
import ru.yandex.repository.ClientRepository;
import ru.yandex.repository.TransactionRepository;

import java.time.LocalDateTime;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    @Bean
    CommandLineRunner initData(ClientRepository clientRepo, AccountRepository accountRepo, TransactionRepository txRepo) {
        return args -> {
            for (long i = 1; i < 10; i++) {
                Client c = clientRepo.save(Client.builder()
                                .firstName("Name")
                                .secondName("Second")
                                .middleName("Middle" )
                        .build()
                );
                Account a = accountRepo.save(Account.builder().type(AccountType.DEBIT).balance(100000L + i).clientId(c.getId()).build());
                txRepo.save(Transaction.builder().accountId(a.getAccountId()).amount(100000L + i).timestamp(LocalDateTime.now()).build());
            }
        };
    }
}


