package com.example.projectapi.infra.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) return false;

        boolean length = password.length() >= 8;
        boolean upper = password.matches(".*[A-Z].*");
        boolean number = password.matches(".*[0-9].*");

        return length && upper && number;
    }
}
