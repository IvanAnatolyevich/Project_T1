package ru.yandex.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {
    @Value("${cache.seconds}")
    private long ttlSeconds;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Around("@annotation(ru.yandex.aop.annotation.Cached)")
    public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = generateKey(joinPoint);
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        } else {
            if (entry != null) {
                cache.remove(key);
            }
            Object result = joinPoint.proceed();
            cache.put(key, new CacheEntry(result, ttlSeconds));
            return result;
        }
    }

    private String generateKey(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().toLongString() + Arrays.toString(joinPoint.getArgs());
    }

    private static class CacheEntry {
        private final Object value;
        private final long expiryTime;

        CacheEntry(Object value, long ttlSeconds) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
        Object getValue() {
            return value;
        }
    }
}
