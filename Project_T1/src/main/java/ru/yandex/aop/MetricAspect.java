package ru.yandex.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.model.TimeLimitExceedLog;
import ru.yandex.repository.TimeLimitExceedLogRepository;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final TimeLimitExceedLogRepository repository;
    @Value("${metrics.time-limit-ms}")
    private long timeLimitMs;

    @Around("@annotation(ru.yandex.aop.annotation.Metric)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        if (duration > timeLimitMs) {
            TimeLimitExceedLog log = new TimeLimitExceedLog();
            log.setSignature(joinPoint.getSignature().toLongString());
            log.setExecutionTimeMs(duration);
            log.setTimestamp(LocalDateTime.now());
            repository.save(log);
        }
        return result;
    }

}
