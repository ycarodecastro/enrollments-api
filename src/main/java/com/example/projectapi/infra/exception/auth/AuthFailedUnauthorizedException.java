package com.example.projectapi.infra.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthFailedUnauthorizedException extends RuntimeException {
    public AuthFailedUnauthorizedException() {
        super("Credenciais invalidas.");
    }
}

