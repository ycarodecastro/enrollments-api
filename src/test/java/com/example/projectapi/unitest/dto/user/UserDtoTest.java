package com.example.projectapi.unitest.dto.user;

import com.example.projectapi.application.user.dto.UserRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.example.projectapi.utils.ValidationAssertions.assertOnlyViolationsForPath;

@DisplayName("Unit - DTO - UserRequest")
class UserDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // ---------------------- EMAIL ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"ycaro.com", "ycaro@", "ycaro.", " ", ""})
    @DisplayName("Deve invalidar quando o email for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorEmail(String invalidEmail) {
        var dto = new UserRequestDTO(invalidEmail, "12345678A@a");
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "email",
                "O campo de email não pode ficar vazio.",
                "O email é inválido."
        );
    }

    // ---------------------- PASSWORD ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "semmaiuscula1", "SemNumero", "curta1A", "", " "})
    @DisplayName("Deve invalidar quando a senha nÃƒÆ’Ã‚Â£o seguir as regras")
    void shouldErrorPassword(String invalidPassword) {
        var dto = new UserRequestDTO("ycaro@gmail.com", invalidPassword);
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "password",
                "O campo de senha não pode ficar vazio.",
                "A senha é inválida."
        );
    }
}
