package ru.yandex.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.model.dto.TransactionAcceptDto;
import ru.yandex.service.TransactionAnalysisService;


@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionAcceptListener {
    private final TransactionAnalysisService analysisService;

    @KafkaListener(topics = "t1_demo_transaction_accept", groupId = "service2_group")
    public void listen(TransactionAcceptDto dto) {
        analysisService.analyze(dto);
    }

}
