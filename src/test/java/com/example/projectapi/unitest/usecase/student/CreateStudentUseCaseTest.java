package com.example.projectapi.unitest.usecase.student;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentRequestDTO;
import com.example.projectapi.application.student.usecase.CreateStudentUseCase;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.exception.student.StudentCpfAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateStudentUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @Mock private StudentConverter studentConverter;

    @InjectMocks
    private CreateStudentUseCase createStudentUseCase;

    @Test
    @DisplayName("Deve criar estudante com sucesso")
    void shouldCreateStudentSuccessfully() {
        // GIVEN
        var dto = createFakeStudentRequestDTO();
        var entity = new StudentEntity();

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(studentRepository.existsByCpf(any())).thenReturn(false);
        when(studentRepository.existsByRg(any())).thenReturn(false);
        when(studentConverter.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode(any())).thenReturn("hashed_pw");
        when(studentRepository.save(any())).thenReturn(entity);

        // WHEN
        createStudentUseCase.execute(dto);

        // THEN
        verify(studentRepository, times(1)).save(any());
        verify(passwordEncoder).encode(dto.user().password());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já existe")
    void shouldThrowExceptionWhenCpfExists() {
        // GIVEN
        var dto = createFakeStudentRequestDTO();
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(studentRepository.existsByCpf(dto.cpf())).thenReturn(true);

        // WHEN & THEN
        assertThatThrownBy(() -> createStudentUseCase.execute(dto))
                .isInstanceOf(StudentCpfAlreadyExistsException.class);

        verify(studentRepository, never()).save(any());
    }

    private StudentRequestDTO createFakeStudentRequestDTO() {
        UserRequestDTO user = new UserRequestDTO("aluno@email.com", "SenhaValida1");
        AddressRequestDTO address = new AddressRequestDTO(
                "60000000",
                "Rua X",
                "Centro",
                "Fortaleza",
                StateUF.CE,
                100,
                null
        );

        return new StudentRequestDTO(
                "Joao da Silva",
                "123456789",
                "39053344705",
                LocalDate.of(2005, 5, 10),
                user,
                address,
                null
        );
    }
}
