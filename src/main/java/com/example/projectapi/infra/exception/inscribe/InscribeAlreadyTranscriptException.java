package com.example.projectapi.infra.exception.inscribe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InscribeAlreadyTranscriptException extends RuntimeException {
    public InscribeAlreadyTranscriptException() {
        super("O estudante já possui um boletim.");
    }
}
