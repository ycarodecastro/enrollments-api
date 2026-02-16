package com.example.projectapi.infra.validation.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // TYPE significa que vai em cima da Classe/Record
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {
    String message() default "A data de término deve ser após a data de início";
    String startField(); // Nome do campo de início
    String endField();   // Nome do campo de fim
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}