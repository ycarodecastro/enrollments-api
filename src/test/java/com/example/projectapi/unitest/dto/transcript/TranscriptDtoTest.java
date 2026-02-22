package com.example.projectapi.unitest.dto.transcript;

import com.example.projectapi.application.transcript.dto.TranscriptRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static com.example.projectapi.utils.ValidationAssertions.assertOnlyViolationsForPath;

@DisplayName("Unit - DTO - TranscriptRequest")
class TranscriptDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // ---------------------- SCHOOL YEAR ----------------------
    @Test
    @DisplayName("Deve invalidar schoolYear quando for nulo")
    void shouldErrorSchoolYearWhenNull() {
        var dto = new TranscriptRequestDTO(null);
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(violations, "schoolYear");
    }

    @Test
    @DisplayName("Deve invalidar schoolYear quando for futuro")
    void shouldErrorSchoolYearWhenFuture() {
        var dto = new TranscriptRequestDTO(Year.now().plusYears(1));
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(violations, "schoolYear");
    }
}
