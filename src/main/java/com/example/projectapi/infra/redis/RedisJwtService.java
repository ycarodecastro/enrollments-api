package com.example.projectapi.infra.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisJwtService {

    private final StringRedisTemplate redis;

    public RedisJwtService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String buildKey(String token){
        return "jwt:blacklist:" + token;
    }

    public void blacklist(String jti, long ttl){

        redis.opsForValue()
                .set(buildKey(jti), "1", ttl, TimeUnit.MILLISECONDS);
    }


    public boolean isBlacklisted(String jti){

        return Boolean.TRUE.equals(redis.hasKey(buildKey(jti))
        );
    }
}

