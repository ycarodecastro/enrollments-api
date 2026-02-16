package com.example.projectapi.unitest.school;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.usecase.CreateSchoolUseCase;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateSchoolUseCaseTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SchoolConverter schoolConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateSchoolUseCase useCase;

    private UserRequestDTO userDTO;
    private AddressRequestDTO addressDTO;
    private SchoolRequestDTO schoolDTO;

    @BeforeEach
    void setup() {
        userDTO = new UserRequestDTO("teste@gmail.com", "12345678");
        addressDTO = new AddressRequestDTO("12345678", "Rua A", "Bairro B", "Sao Paulo", StateUF.SP, 100, "");
        schoolDTO = new SchoolRequestDTO("Patronato", "12345678900011", userDTO, addressDTO);
    }

    @Test
    void shouldCreateSchoolSuccessfully() {
        // Simula que o email e o CNPJ não existem
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(false);
        when(schoolRepository.existsByCnpj(schoolDTO.cnpj())).thenReturn(false);

        // Mocka o converter de DTO para Entity
        when(schoolConverter.toEntity(schoolDTO)).thenAnswer(invocation -> {
            SchoolRequestDTO dto = invocation.getArgument(0);
            SchoolEntity entity = new SchoolEntity();
            entity.setName(dto.name());
            entity.setCnpj(dto.cnpj());
            return entity;
        });

        // Mocka o PasswordEncoder
        when(passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");

        // Mocka o repository salvar
        when(schoolRepository.save(any(SchoolEntity.class))).thenAnswer(invocation -> {
            SchoolEntity entity = invocation.getArgument(0);
            entity.setId(1L); // Simula o ID gerado pelo DB
            return entity;
        });

        // Mocka o converter de Entity para ResponseDTO
        when(schoolConverter.toResponseDTO(any(SchoolEntity.class))).thenAnswer(invocation -> {
            SchoolEntity entity = invocation.getArgument(0);
            return new SchoolResponseDTO(entity.getId(), entity.getName(), entity.getUser() != null ? entity.getUser().getEmail() : null, null);
        });

        // Executa o useCase
        SchoolResponseDTO result = useCase.execute(schoolDTO);

        // Verificações
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(schoolDTO.name(), result.name());
        assertEquals(userDTO.email(), result.email());

        // Verifica que o repository foi chamado
        verify(schoolRepository, times(1)).save(any(SchoolEntity.class));
        verify(passwordEncoder, times(1)).encode(userDTO.password());
    }

    @Test
    void shouldThrowExceptionWhenCnpjAlreadyExists() {
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(false);
        when(schoolRepository.existsByCnpj(schoolDTO.cnpj())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> useCase.execute(schoolDTO));
        assertTrue(exception.getMessage().contains("CNPJ já cadastrado"));

        verify(schoolRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(userDTO.email())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> useCase.execute(schoolDTO));
        assertTrue(exception.getMessage().contains("Email já cadastrado"));

        verify(schoolRepository, never()).save(any());
    }
}
