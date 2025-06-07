package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.model.TransactionStatus;
import ru.yandex.model.dto.TransactionAcceptDto;
import ru.yandex.model.dto.TransactionResultDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionAnalysisServiceImpl implements TransactionAnalysisService {
    private final Map<String, List<TransactionAcceptDto>> history = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${transaction.block.threshold}")
    private int threshold;
    @Value("${transaction.analysis.limit.time.ms}")
    private long window;
    @Value("${kafka.topics.result}")
    private String resultTopic;

    public void analyze(TransactionAcceptDto dto) {
        String key = dto.getClientId() + ":" + dto.getAccountId();
        history.putIfAbsent(key, new ArrayList<>());

        List<TransactionAcceptDto> txs = history.get(key);
        txs.add(dto);

        txs.removeIf(tx -> tx.getTimestamp().isBefore(LocalDateTime.now().minusSeconds(window)));

        if (txs.size() >= threshold) {
            for (TransactionAcceptDto tx : txs) {
                kafkaTemplate.send(resultTopic, new TransactionResultDto(
                        tx.getTransactionId(),
                        tx.getAccountId(),
                        TransactionStatus.BLOCKED
                ));
            }
            txs.clear();
            return;
        }

        if (dto.getAmount().compareTo(dto.getBalance()) < 0) {
            kafkaTemplate.send(resultTopic, new TransactionResultDto(
                    dto.getTransactionId(),
                    dto.getAccountId(),
                    TransactionStatus.REJECTED
            ));
            return;
        }

        kafkaTemplate.send(resultTopic, new TransactionResultDto(
                dto.getTransactionId(),
                dto.getAccountId(),
                TransactionStatus.ACCECPTED
        ));
    }
}
