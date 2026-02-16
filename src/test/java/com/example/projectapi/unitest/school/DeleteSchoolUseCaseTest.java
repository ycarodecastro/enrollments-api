package com.example.projectapi.unitest.school;

import com.example.projectapi.application.school.usecase.DeleteSchoolUseCase;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
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
public class DeleteSchoolUseCaseTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private DeleteSchoolUseCase useCase;

    private SchoolEntity schoolEntity;

    @BeforeEach
    void setup() {

        schoolEntity = new SchoolEntity();
        schoolEntity.setId(1L);
        schoolEntity.setName("Patronato");
        schoolEntity.setCnpj("12345678900011");
    }

    @Test
    void shouldDeleteSchoolSuccessfully() {

        // Mock: escola existe
        when(schoolRepository.findById(1L))
                .thenReturn(Optional.of(schoolEntity));

        // Executa
        useCase.execute(1L);

        // Verifica que buscou pelo ID
        verify(schoolRepository, times(1)).findById(1L);

        // Verifica que deletou corretamente
        verify(schoolRepository, times(1)).delete(schoolEntity);
    }

    @Test
    void shouldThrowExceptionWhenSchoolNotFound() {

        // Mock: escola não existe
        when(schoolRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Executa e espera exception
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(1L));

        // Confirma mensagem
        assertTrue(exception.getMessage().contains("Escola não encontrado"));

        // Delete nunca pode ser chamado
        verify(schoolRepository, never()).delete(any());
    }
}
