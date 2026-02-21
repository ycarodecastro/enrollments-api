package com.example.projectapi.infra.exception.transcript;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TranscriptNotFoundException extends RuntimeException {

    public TranscriptNotFoundException() {
        super("Boletim nao encontrado.");
    }
}

