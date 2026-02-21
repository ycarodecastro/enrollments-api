package com.example.projectapi.infra.exception.redis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class RedisFailedResetKey extends RuntimeException {
    public RedisFailedResetKey() {
        super("Falha ao resetar chave de rate limit no Redis.");
    }
}

