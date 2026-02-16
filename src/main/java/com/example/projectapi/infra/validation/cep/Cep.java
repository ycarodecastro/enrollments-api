package com.example.projectapi.infra.validation.cep;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CepValidator.class)
public @interface Cep {
    String message() default "CEP inválido. Use o formato 00000-000 ou apenas números.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}