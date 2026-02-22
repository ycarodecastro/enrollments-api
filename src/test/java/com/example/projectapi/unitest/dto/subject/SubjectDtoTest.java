package com.example.projectapi.unitest.dto.subject;

import com.example.projectapi.application.subject.dto.SubjectRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.example.projectapi.utils.ValidationAssertions.assertOnlyViolationsForPath;

@DisplayName("Unit - DTO - SubjectRequest")
class SubjectDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // ---------------------- NAME ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Deve invalidar quando a materia for invÃƒÆ’Ã‚Â¡lida")
    void shouldErrorName(String invalidName) {
        var dto = new SubjectRequestDTO(invalidName);
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(violations, "name");
    }
}
