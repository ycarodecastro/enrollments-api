package com.example.projectapi.infra.validation.rg;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RGValidator.class) // Liga esta anotação à lógica do validador
public @interface Rg {
    String message() default "RG inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}