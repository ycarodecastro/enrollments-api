package com.example.projectapi.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redis;

    public void set(
            String key,
            String value,
            Long ttl
    ) {
        redis.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }

    public String get(String key) {
        return redis.opsForValue().get(key);
    }

    public Boolean exists(String key) {
        return redis.hasKey(key);
    }

    public Long increment(String key) { return redis.opsForValue().increment(key); }

    public void expire(String key, Duration ttl){redis.expire(key, ttl) ;}

    public void delete(String key) {
        redis.delete(key);
    }

}
