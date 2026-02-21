package com.example.projectapi.unitest.dto.school;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.StateUF;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.example.projectapi.unitest.utils.ValidationAssertions.assertOnlyViolationsForPath;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit - DTO - SchoolRequest")
public class SchoolDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // ---------------------- VALID ----------------------
    @Test
    @DisplayName("Deve validar quando todos os campos do dto estiverem corretos")
    void shouldValidateSuccessfully() {
        var dto = createValidSchoolRequest("Escola Exemplo", "12345678000195", createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    // ---------------------- NAME ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "Jo", "A"})
    @DisplayName("Deve invalidar quando o nome for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorName(String invalidName) {
        var dto = createValidSchoolRequest(invalidName, "12345678000195", createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "name",
                "O campo de nome não pode ficar vazio.",
                "O nome deve ter entre 3 e 120 digitos."
        );
    }

    // ---------------------- CNPJ ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "11111111111111", "1234567890123", "abc"})
    @DisplayName("Deve invalidar quando o cnpj for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorCnpj(String invalidCnpj) {
        var dto = createValidSchoolRequest("Escola Exemplo", invalidCnpj, createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "cnpj",
                "O campo de cnpj não pode ficar vazio.",
                "CNPJ inválido."
        );
    }

    // ---------------------- USER ----------------------
    @Test
    @DisplayName("Deve invalidar quando o User for nulo")
    void shouldErrorUserWhenNull() {
        var dto = createValidSchoolRequest("Escola Exemplo", "12345678000195", null, createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "user",
                "não deve ser nulo"
        );
    }

    // ---------------------- ADDRESS ----------------------
    @Test
    @DisplayName("Deve invalidar quando o Address for nulo")
    void shouldErrorAddressWhenNull() {
        var dto = createValidSchoolRequest("Escola Exemplo", "12345678000195", createValidUser(), null);
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "address",
                "não deve ser nulo"
        );
    }

    private SchoolRequestDTO createValidSchoolRequest(String name, String cnpj, UserRequestDTO user, AddressRequestDTO address) {
        return new SchoolRequestDTO(name, cnpj, user, address);
    }

    private UserRequestDTO createValidUser() {
        return new UserRequestDTO("valid@email.com", "SenhaValida1");
    }

    private AddressRequestDTO createValidAddress() {
        return new AddressRequestDTO("60000000", "Rua X", "Centro", "Cidade", StateUF.CE, 123, null);
    }
}
