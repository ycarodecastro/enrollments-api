package com.example.projectapi.infra.exception.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentEmailAlreadyExistsException extends RuntimeException {

    public StudentEmailAlreadyExistsException() {
        super("Email já cadastrado.");
    }
}
