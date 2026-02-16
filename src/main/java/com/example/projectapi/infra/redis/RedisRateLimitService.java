package com.example.projectapi.infra.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisRateLimitService {

    private static final Logger log = LoggerFactory.getLogger(RedisRateLimitService.class);

    private final StringRedisTemplate redis;

    public RedisRateLimitService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public long incrementAttempts(String key, Duration ttl){
        try {
            Long attempts = redis.opsForValue().increment(key);

            if(attempts != null && attempts == 1){
                redis.expire(key, ttl);
            }

            return attempts == null ? 0 : attempts;
        } catch (RuntimeException ex) {
            log.warn("Falha ao incrementar tentativas no Redis para chave {}", key, ex);
            return 0;
        }
    }

    public boolean isBlocked(String key, int maxAttempts){
        try {
            String value = redis.opsForValue().get(key);

            if(value == null) return false;

            return Integer.parseInt(value) >= maxAttempts;
        } catch (RuntimeException ex) {
            log.warn("Falha ao consultar bloqueio no Redis para chave {}", key, ex);
            return false;
        }
    }

    public void reset(String key){
        try {
            redis.delete(key);
        } catch (RuntimeException ex) {
            log.warn("Falha ao resetar chave de rate-limit no Redis para chave {}", key, ex);
        }
    }
}
