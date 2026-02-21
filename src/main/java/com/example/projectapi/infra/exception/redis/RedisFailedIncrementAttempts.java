package com.example.projectapi.infra.exception.redis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class RedisFailedIncrementAttempts extends RuntimeException {
    public RedisFailedIncrementAttempts() {
        super("Falha ao incrementar tentativas no Redis.");
    }
}

