package com.example.projectapi.infra.exception.school;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SchoolNotFoundException extends RuntimeException {

    public SchoolNotFoundException() {
        super("Escola não encontrada.");
    }
}
