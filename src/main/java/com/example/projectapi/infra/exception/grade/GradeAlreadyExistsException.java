package com.example.projectapi.infra.exception.grade;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GradeAlreadyExistsException extends RuntimeException {

    public GradeAlreadyExistsException() {
        super("Nota ja cadastrada para essa materia e periodo neste boletim.");
    }
}
