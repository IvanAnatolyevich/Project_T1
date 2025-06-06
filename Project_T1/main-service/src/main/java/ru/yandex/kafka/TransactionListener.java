package ru.yandex.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.model.dto.TransactionDto;
import ru.yandex.service.TransactionService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionListener {
    private final TransactionService transactionService;

    @KafkaListener(topics = "t1_demo_transactions", groupId = "t1_group")
    public void handleTransaction(TransactionDto transactionDto) {
        transactionService.processIncomingTransaction(transactionDto);
    }

}
