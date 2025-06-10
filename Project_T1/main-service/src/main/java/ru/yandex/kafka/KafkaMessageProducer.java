package ru.yandex.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProducer {
    private final KafkaTemplate kafkaTemplate;

    public void send(String topic, String headerType, String message) {
        try {
            kafkaTemplate.send(topic, null, null, message, Map.of("errorType", headerType));
        } catch (Exception e) {
            log.error("Ошибка при отправке Kafka-сообщения: {}", e.getMessage());
            throw new RuntimeException("Kafka отправка не удалась", e);
        }
    }
}
