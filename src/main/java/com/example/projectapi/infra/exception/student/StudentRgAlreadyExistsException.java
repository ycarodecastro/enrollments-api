package com.example.projectapi.infra.exception.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentRgAlreadyExistsException extends RuntimeException {

    public StudentRgAlreadyExistsException() {
        super("RG ja cadastrado.");
    }
}

