package com.example.projectapi.unitest.school;

import com.example.projectapi.application.address.converter.AddressConverter;
import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.dto.SchoolUpdateDTO;
import com.example.projectapi.application.school.usecase.UpdateSchoolUseCase;
import com.example.projectapi.application.user.converter.UserConverter;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateSchoolUseCaseTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolConverter schoolConverter;

    @Mock
    private AddressConverter addressConverter;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UpdateSchoolUseCase useCase;

    private SchoolEntity schoolEntity;
    private SchoolUpdateDTO updateDTO;

    @BeforeEach
    void setup() {

        // ===== ENTIDADE EXISTENTE =====
        schoolEntity = new SchoolEntity();
        schoolEntity.setId(1L);
        schoolEntity.setName("Patronato");
        schoolEntity.setCnpj("12345678900011");

        // Address e User obrigatórios no SchoolEntity
        schoolEntity.setAddress(
                AddressEntity.builder()
                        .cep("12345678")
                        .city("São Paulo")
                        .state(StateUF.SP)
                        .street("Rua A")
                        .neighborhood("Centro")
                        .number(100)
                        .complement("")
                        .build()
        );

        schoolEntity.setUsers(
                UserEntity.builder()
                        .email("old@gmail.com")
                        .password("123")
                        .build()
        );

        // ===== DTO DE UPDATE =====
        updateDTO = new SchoolUpdateDTO(
                "Escola Atualizada", // novo nome
                null, // não vamos atualizar cnpj agora

                new UserRequestDTO("new@gmail.com", "123456"),
                new AddressRequestDTO(
                        "99999999",
                        "Rua Nova",
                        "Bairro Novo",
                        "Rio de Janeiro",
                        StateUF.RJ,
                        200,
                        "Casa"
                )
        );
    }

    @Test
    void shouldUpdateSchoolSuccessfully() {

        // Mock findById
        when(schoolRepository.findById(1L))
                .thenReturn(Optional.of(schoolEntity));

        // Mock save
        when(schoolRepository.save(any(SchoolEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock ResponseDTO
        when(schoolConverter.toResponseDTO(any(SchoolEntity.class)))
                .thenAnswer(invocation -> {
                    SchoolEntity entity = invocation.getArgument(0);
                    return new SchoolResponseDTO(
                            entity.getId(),
                            entity.getName(),
                            entity.getUsers().getEmail(),
                            null
                    );
                });

        // ===== EXECUTA =====
        SchoolResponseDTO result = useCase.execute(1L, updateDTO);

        // ===== ASSERTS =====
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Patronato", schoolEntity.getName());
        // ⚠️ O nome não muda sozinho porque converter está mockado

        // ===== VERIFY =====
        verify(schoolRepository, times(1)).findById(1L);

        verify(schoolConverter, times(1))
                .updateEntityFromDTO(updateDTO, schoolEntity);

        verify(addressConverter, times(1))
                .updateEntityFromDTO(updateDTO.address(), schoolEntity.getAddress());

        verify(userConverter, times(1))
                .updateEntityFromDTO(updateDTO.user(), schoolEntity.getUsers());

        verify(schoolRepository, times(1)).save(schoolEntity);

        verify(schoolConverter, times(1)).toResponseDTO(any());
    }

    @Test
    void shouldThrowExceptionWhenSchoolNotFound() {

        when(schoolRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(1L, updateDTO));

        assertTrue(exception.getMessage().contains("Escola não encontrado"));

        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, never()).save(any());
    }

    @Test
    void shouldUpdateSchoolWithoutUpdatingUserOrAddress() {

        SchoolUpdateDTO dtoWithoutRelations =
                new SchoolUpdateDTO("Novo Nome", null, null, null);

        when(schoolRepository.findById(1L))
                .thenReturn(Optional.of(schoolEntity));

        when(schoolRepository.save(any(SchoolEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(schoolConverter.toResponseDTO(any(SchoolEntity.class)))
                .thenReturn(new SchoolResponseDTO(
                        1L,
                        "Novo Nome",
                        "old@gmail.com",
                        null
                ));

        // EXECUTA
        SchoolResponseDTO result =
                useCase.execute(1L, dtoWithoutRelations);

        assertNotNull(result);

        // Verify: só atualiza entidade principal
        verify(schoolConverter, times(1))
                .updateEntityFromDTO(dtoWithoutRelations, schoolEntity);

        // NÃO chama converters secundários
        verify(addressConverter, never()).updateEntityFromDTO(any(), any());
        verify(userConverter, never()).updateEntityFromDTO(any(), any());

        verify(schoolRepository, times(1)).save(schoolEntity);
    }
}
