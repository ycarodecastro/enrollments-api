package com.example.projectapi.infra.exception.inscribe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InscribeNotFoundException extends RuntimeException {

    public InscribeNotFoundException() {
        super("Inscricao nao encontrada.");
    }
}
