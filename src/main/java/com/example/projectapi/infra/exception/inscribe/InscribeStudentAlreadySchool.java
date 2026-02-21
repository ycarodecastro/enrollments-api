package com.example.projectapi.infra.exception.inscribe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InscribeStudentAlreadySchool extends RuntimeException {
    public InscribeStudentAlreadySchool() {

        super("O estudante ja esta vinculado a uma escola.");
    }
}

