package com.example.projectapi.infra.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.startField();
        this.endFieldName = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // Pega os valores dos campos via Reflection
            var startField = value.getClass().getDeclaredField(startFieldName);
            var endField = value.getClass().getDeclaredField(endFieldName);
            startField.setAccessible(true);
            endField.setAccessible(true);

            LocalDate start = (LocalDate) startField.get(value);
            LocalDate end = (LocalDate) endField.get(value);

            if (start == null || end == null) return true;

            return !end.isBefore(start);
        } catch (Exception e) {
            return false;
        }
    }
}