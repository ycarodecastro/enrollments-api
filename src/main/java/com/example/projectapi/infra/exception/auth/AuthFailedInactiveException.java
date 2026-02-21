package com.example.projectapi.infra.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthFailedInactiveException extends RuntimeException {
    public AuthFailedInactiveException() {
        super("Sua conta esta inativa.");
    }
}

