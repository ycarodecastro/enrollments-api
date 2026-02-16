package com.example.projectapi.infra.exception.grade;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GradeNotFoundException extends RuntimeException {

    public GradeNotFoundException() {
        super("Nota nao encontrada.");
    }
}
