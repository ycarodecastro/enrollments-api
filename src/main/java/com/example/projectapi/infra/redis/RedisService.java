package com.example.projectapi.infra.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate redis;


    // função de serviço do redis;
    public RedisService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    // função de salvar um valor por tempo determinado;
    public void set(String key, String value, Duration ttl) {
        redis.opsForValue().set(key, value, ttl);
    }

    // função que busca um valor;
    public String get(String key) {
        return redis.opsForValue().get(key);
    }

    // função que verifica a existencia;
    public Boolean exists(String key) {
        return redis.hasKey(key);
    }

    // deleta o valor e a chave;
    public void delete(String key) {
        redis.delete(key);
    }

}
