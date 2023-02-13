package com.wangkisa.commerce.domain.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(final Long key) {
        String generatedKey = generateKey(key);
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generatedKey, "lock", Duration.ofMillis(3_000));
    }
    public Boolean unlock(final Long key) {
        String generatedKey = generateKey(key);
        return redisTemplate.delete(generatedKey);
    }
    public String generateKey(final Long key) {
        return key.toString();
    }
}
