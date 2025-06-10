package ru.yandex.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.model.Account;
import ru.yandex.model.AccountStatus;
import ru.yandex.model.Transaction;
import ru.yandex.model.dto.TransactionResultDto;
import ru.yandex.repository.AccountRepository;
import ru.yandex.repository.TransactionRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionResultListener {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "t1_demo_transaction_result", groupId = "t1_group")
    public void processResult(TransactionResultDto dto) {
        Transaction transaction = transactionRepository.findByTransactionId(dto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
        transaction.setStatus(dto.getStatus());
        transactionRepository.save(transaction);
        Account account = accountRepository.findByAccountId(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        switch (dto.getStatus()) {
            case REJECTED -> account.setBalance(account.getBalance() - transaction.getAmount());
            case BLOCKED -> {
                account.setStatus(AccountStatus.BLOCKED);
                if (transaction.getAmount() < 0) {
                    account.setFrozenAmount(account.getFrozenAmount() - transaction.getAmount());
                } else {
                    account.setFrozenAmount(account.getFrozenAmount() + transaction.getAmount());
                }
            }
        }
        accountRepository.save(account);
    }
}
