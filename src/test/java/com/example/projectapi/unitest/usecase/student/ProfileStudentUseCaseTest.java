package com.example.projectapi.unitest.usecase.student;

import com.example.projectapi.application.address.dto.AddressResponseDTO;
import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.application.student.usecase.ProfileStudentUseCase;
import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - Student - Profile UseCase")
class ProfileStudentUseCaseTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentConverter studentConverter;

    @InjectMocks
    private ProfileStudentUseCase profileStudentUseCase;

    @Test
    @DisplayName("Deve retornar o perfil do estudante com sucesso")
    void shouldReturnStudentProfileSuccessfully() {
        // 1. GIVEN - Preparação dos dados
        var currentUser = UserEntity.builder().id(1L).email("ycaro@email.com").build();

        // Criando a Entidade de Endereço (Entity)
        var addressEntity = AddressEntity.builder()
                .street("Rua Exemplo")
                .number(12) // Verifique se no seu modelo é String ou Integer
                .city("Fortaleza")
                .build();

        // Criando a Entidade de Estudante (Entity)
        var studentEntity = new StudentEntity();
        studentEntity.setId(1L);
        studentEntity.setName("Ycaro");
        studentEntity.setCpf("123.456.789-00");
        studentEntity.setDateBirth(LocalDate.of(2000, 1, 1));
        studentEntity.setAddress(addressEntity); // Agora passamos a ENTITY

        // Criando o DTO de Resposta de Endereço (DTO)
        var addressResponse = new AddressResponseDTO(
                1L,
                "60000-000",
                "Rua Exemplo",
                "Fazendinha",
                "Fortaleza",
                StateUF.CE,
                12, // Campos exemplo que costumam ter no DTO
                null
        );

        // Criando o DTO de Resposta do Estudante (DTO)
        var expectedResponse = new StudentResponseDTO(
                1L,
                "Ycaro",
                "ycaro@email.com",
                LocalDate.of(2000, 1, 1),
                addressResponse // Agora passamos o DTO
        );

        // Configurando os Mocks
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(studentEntity));
        when(studentConverter.toResponseDTO(studentEntity)).thenReturn(expectedResponse);

        // 2. WHEN - Execução
        var response = profileStudentUseCase.execute(currentUser);

        // 3. THEN - Verificações
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Ycaro");
        assertThat(response.address().street()).isEqualTo("Rua Exemplo");

        verify(studentRepository, times(1)).findByUserId(1L);
        verify(studentConverter, times(1)).toResponseDTO(studentEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário atual for nulo")
    void shouldThrowExceptionWhenUserIsNull() {
        // GIVEN
        UserEntity currentUser = null;

        // WHEN & THEN
        assertThatThrownBy(() -> profileStudentUseCase.execute(currentUser))
                .isInstanceOf(StudentNotFoundException.class);

        // Garante que nem tentou ir no banco
        verifyNoInteractions(studentRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o estudante não for encontrado no banco")
    void shouldThrowExceptionWhenStudentNotFound() {
        // GIVEN
        var currentUser = UserEntity.builder().id(1L).build();

        // Simulamos o banco retornando vazio
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> profileStudentUseCase.execute(currentUser))
                .isInstanceOf(StudentNotFoundException.class);
    }
}
