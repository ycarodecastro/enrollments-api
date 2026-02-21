package com.example.projectapi.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // Anotação do Spring que registra essa classe como um "serviço" no contexto da aplicação.
@RequiredArgsConstructor
public class RedisJwtService {

    private final RedisService redisService;

    private String buildKey(String token){
        return "jwt:blacklist:" + token;
    }

    public void blacklist(String jti, long ttl){
        redisService.set(buildKey(jti), "1", ttl);
    }

    public boolean isBlacklisted(String jti){
        return Boolean.TRUE.equals(redisService.exists(buildKey(jti)));
    }
}