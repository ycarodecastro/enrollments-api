package com.example.projectapi.infra.validation.cep;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<Cep, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // Deixa o @NotBlank cuidar disso

        // Regex profissional para CEP brasileiro
        return value.matches("^\\d{5}-?\\d{3}$");
    }
}