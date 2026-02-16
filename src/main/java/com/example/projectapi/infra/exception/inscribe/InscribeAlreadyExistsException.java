package com.example.projectapi.infra.exception.inscribe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InscribeAlreadyExistsException extends RuntimeException {

    public InscribeAlreadyExistsException() {
        super("Você já realizou uma inscrição para esta oferta e não pode tentar novamente.");
    }
}
