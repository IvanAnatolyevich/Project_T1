package ru.yandex.aop;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.yandex.model.DataSourceErrorLog;
import ru.yandex.repository.DataSourceErrorLogRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceAspect {
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Around("@annotation(LogDataSourceError)")
    public Object logErrors(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            DataSourceErrorLog log = new DataSourceErrorLog();
            log.setMessage(e.getMessage());
            log.setSignature(proceedingJoinPoint.getSignature().toLongString());
            log.setTextStackTrace(ExceptionUtils.getStackTrace(e));
            dataSourceErrorLogRepository.save(log);
            throw e;
        }
    }

}
