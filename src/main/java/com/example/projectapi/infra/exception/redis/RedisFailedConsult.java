package com.example.projectapi.infra.exception.redis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class RedisFailedConsult extends RuntimeException {
    public RedisFailedConsult() {
        super("Falha ao consultar bloqueio no Redis.");
    }
}

