package com.example.projectapi.infra.exception.subject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends RuntimeException {

    public SubjectNotFoundException() {
        super("Materia nao encontrada para esta escola.");
    }
}

