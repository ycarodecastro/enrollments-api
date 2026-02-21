package com.example.projectapi.infra.redis;

import com.example.projectapi.infra.exception.redis.RedisFailedConsult;
import com.example.projectapi.infra.exception.redis.RedisFailedIncrementAttempts;
import com.example.projectapi.infra.exception.redis.RedisFailedResetKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRateLimitService {

    private final RedisService redisService;

    public void incrementAttempts(String key, Duration ttl) {
        try {
            Long attempts = redisService.increment(key);

            if (attempts != null && attempts == 1) {
                redisService.expire(key, ttl);
            }
        } catch (RuntimeException ex) {
            log.warn("Falha ao incrementar tentativas no Redis para chave {}.", key, ex);
            throw new RedisFailedIncrementAttempts();
        }
    }

        public boolean isBlocked(String key, int maxAttempts){
        try {
            String value = redisService.get(key);
            return value != null && Integer.parseInt(value) >= maxAttempts;

        } catch (Exception ex) {
            log.warn("Falha ao consultar bloqueio no Redis para chave {}", key, ex);
            throw new RedisFailedConsult();
        }
    }

    // Metodo que reseta (remove) a chave do Redis.
    // Isso limpa o contador de tentativas, permitindo que o usuário/IP volte a tentar.
    public void reset(String key){
        try {
            redisService.delete(key);

        } catch (RuntimeException ex) {
            log.warn(
                    "Falha ao resetar chave de rate-limit no Redis para chave {}",
                    key, ex
            );

            throw new RedisFailedResetKey();
        }
    }
}
