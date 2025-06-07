package ru.yandex.aop;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.kafka.KafkaMessageProducer;
import ru.yandex.model.DataSourceErrorLog;
import ru.yandex.repository.DataSourceErrorLogRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceAspect {
    private final KafkaMessageProducer producer;
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Value("${metric.kafka.topic}")
    String topic;

    @Around("within(@ru.yandex.aop.annotation.LogDataSourceError *)")
    public Object logErrors(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            String message = "Ошибка в методе: " + proceedingJoinPoint.getSignature() +
                    "\nСообщение: " + e.getMessage();
            try {
                producer.send(topic, "DATA_SOURCE", message);
            } catch (Exception kafkaException) {
                DataSourceErrorLog log = new DataSourceErrorLog();
                log.setMessage(e.getMessage());
                log.setSignature(proceedingJoinPoint.getSignature().toLongString());
                log.setTextStackTrace(ExceptionUtils.getStackTrace(e));
                dataSourceErrorLogRepository.save(log);
            }
            throw e;
        }
    }

}
