package com.example.projectapi.unitest.dto.student;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.student.dto.StudentRequestDTO;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.StateUF;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static com.example.projectapi.unitest.utils.ValidationAssertions.assertOnlyViolationsForPath;

@DisplayName("Unit - DTO - StudentRequest")
public class StudentDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // ---------------------- NAME ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "Jo", "A"})
    @DisplayName("Deve invalidar quando o nome for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorName(String invalidName) {
        var dto = createValidStudentRequest(invalidName, "123456789", "39053344705", LocalDate.now().minusYears(18), createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "name",
                "O campo de nome não pode ficar vazio.",
                "O campo de nome deve conter entre 3 a 120 caracteres."
        );
    }

    // ---------------------- RG ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "abc123", "123", "213.1234.49"})
    @DisplayName("Deve invalidar quando o RG for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorRg(String invalidRg) {
        var dto = createValidStudentRequest("Joao da Silva", invalidRg, "39053344705", LocalDate.now().minusYears(18), createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "rg",
                "O campo de rg não pode ficar vazio.",
                "Rg inválido."
        );
    }

    // ---------------------- CPF ----------------------
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "11111111111", "1234567890", "abc"})
    @DisplayName("Deve invalidar quando o CPF for invÃƒÆ’Ã‚Â¡lido")
    void shouldErrorCpf(String invalidCpf) {
        var dto = createValidStudentRequest("Joao da Silva", "123456789", invalidCpf, LocalDate.now().minusYears(18), createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "cpf",
                "O campo de cpf não pode ficar vazio.",
                "CPF inválido."
        );
    }

    // ---------------------- DATE OF BIRTH ----------------------
    @Test
    @DisplayName("Deve invalidar quando a data de nascimento for futura")
    void shouldErrorDateBirthWhenFuture() {
        var dto = createValidStudentRequest("Joao da Silva", "123456789", "39053344705", LocalDate.now().plusDays(1), createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "dateBirth",
                "A data de nascimento deve ser uma data no passado."
        );
    }

    @Test
    @DisplayName("Deve invalidar quando a data de nascimento for nula")
    void shouldErrorDateBirthWhenNull() {
        var dto = createValidStudentRequest("Joao da Silva", "123456789", "39053344705", null, createValidUser(), createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "dateBirth",
                "O campo de data de nascimento não pode ficar vazio."
        );
    }

    // ---------------------- USER ----------------------
    @Test
    @DisplayName("Deve invalidar quando o User for nulo")
    void shouldErrorUser() {
        var dto = createValidStudentRequest("Joao da Silva", "123456789", "39053344705", LocalDate.now().minusYears(18), null, createValidAddress());
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "user",
                "O usuario é inválido."
        );
    }

    // ---------------------- ADDRESS ----------------------
    @Test
    @DisplayName("Deve invalidar quando o Address for nulo")
    void shouldErrorAddress() {
        var dto = createValidStudentRequest("Joao da Silva", "123456789", "39053344705", LocalDate.now().minusYears(18), createValidUser(), null);
        var violations = validator.validate(dto);
        assertOnlyViolationsForPath(
                violations,
                "address",
                "O endereço é inválido."
        );
    }

    private StudentRequestDTO createValidStudentRequest(String name, String rg, String cpf, LocalDate dateBirth, UserRequestDTO user, AddressRequestDTO address) {
        return new StudentRequestDTO(name, rg, cpf, dateBirth, user, address);
    }

    private UserRequestDTO createValidUser() {
        return new UserRequestDTO("valid@email.com", "SenhaValida1");
    }

    private AddressRequestDTO createValidAddress() {
        return new AddressRequestDTO("60000000", "Rua X", "Centro", "Cidade", StateUF.CE, 123, null);
    }
}
