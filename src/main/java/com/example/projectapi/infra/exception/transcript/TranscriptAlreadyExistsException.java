package com.example.projectapi.infra.exception.transcript;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TranscriptAlreadyExistsException extends RuntimeException {

    public TranscriptAlreadyExistsException() {
        super("Boletim ja cadastrado para este ano letivo.");
    }
}
