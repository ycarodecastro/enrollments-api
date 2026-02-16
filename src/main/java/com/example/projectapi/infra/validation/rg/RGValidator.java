package com.example.projectapi.infra.validation.rg;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RGValidator implements ConstraintValidator<Rg, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Se for nulo, o @NotBlank cuidará disso. Aqui retornamos true para não duplicar erro.
        if (value == null) return true;

        // Como o CleanupDeserializer já limpou o campo,
        // validamos apenas se o resultado final faz sentido (7 a 14 caracteres de 0-9 ou X)
        return value.matches("^[0-9X]{7,14}$");
    }
}