package ru.yandex.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.kafka.KafkaMessageProducer;
import ru.yandex.model.TimeLimitExceedLog;
import ru.yandex.repository.TimeLimitExceedLogRepository;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final KafkaMessageProducer producer;
    private final TimeLimitExceedLogRepository repository;
    @Value("${metric.limit.ms")
    private long timeLimitMs;
    @Value("${metric.kafka.topic}")
    String topic;

    @Around("@annotation(ru.yandex.aop.annotation.Metric)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        if (duration > timeLimitMs) {
            String message = "Метод превысил лимит: " + joinPoint.getSignature() +
                    ", время = " + duration + "ms";
            try {
                producer.send(topic, "METRICS", message);
            } catch (Exception e) {
                TimeLimitExceedLog log = new TimeLimitExceedLog();
                log.setSignature(joinPoint.getSignature().toLongString());
                log.setExecutionTimeMs(duration);
                log.setTimestamp(LocalDateTime.now());
                repository.save(log);
            }
        }
        return result;
    }

}
