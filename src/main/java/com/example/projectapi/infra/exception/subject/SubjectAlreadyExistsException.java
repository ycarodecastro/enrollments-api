package com.example.projectapi.infra.exception.subject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SubjectAlreadyExistsException extends RuntimeException {

    public SubjectAlreadyExistsException() {
        super("Materia ja cadastrada para esta escola.");
    }
}
