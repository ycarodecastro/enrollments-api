package com.example.projectapi.infra.exception.school;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SchoolEmailAlreadyExistsException extends RuntimeException {

    public SchoolEmailAlreadyExistsException() {
        super("Email já cadastrado.");
    }
}
