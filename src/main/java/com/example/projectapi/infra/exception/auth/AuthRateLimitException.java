package com.example.projectapi.infra.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class AuthRateLimitException extends RuntimeException {
    public AuthRateLimitException() {
        super("Muitas tentativas de login. Tente novamente mais tarde.");
    }
}

