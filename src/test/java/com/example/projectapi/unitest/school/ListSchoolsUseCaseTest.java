package com.example.projectapi.unitest.school;

import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.usecase.ProfileSchoolUseCase;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListSchoolsUseCaseTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolConverter schoolConverter;

    @InjectMocks
    private ProfileSchoolUseCase useCase;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 2);
    }

    @Test
    void shouldListSchoolsSuccessfully() {

        // Criando entidades fake
        SchoolEntity school1 = new SchoolEntity();
        school1.setId(1L);
        school1.setName("Patronato");

        SchoolEntity school2 = new SchoolEntity();
        school2.setId(2L);
        school2.setName("Escola Central");

        // Simula Page retornada do repository
        Page<SchoolEntity> pageEntities =
                new PageImpl<>(List.of(school1, school2), pageable, 2);

        // Mock repository
        when(schoolRepository.findAll(pageable)).thenReturn(pageEntities);

        // Mock converter Entity → DTO
        when(schoolConverter.toResponseDTO(any(SchoolEntity.class)))
                .thenAnswer(invocation -> {
                    SchoolEntity entity = invocation.getArgument(0);
                    return new SchoolResponseDTO(
                            entity.getId(),
                            entity.getName(),
                            null,
                            null
                    );
                });

        // Executa o UseCase
        Page<SchoolResponseDTO> result = useCase.execute(pageable);

        // ===== ASSERTS =====

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        assertEquals("Patronato", result.getContent().get(0).name());
        assertEquals("Escola Central", result.getContent().get(1).name());

        // ===== VERIFY =====

        verify(schoolRepository, times(1)).findAll(pageable);
        verify(schoolConverter, times(2)).toResponseDTO(any(SchoolEntity.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoSchoolsExist() {

        // Simula página vazia
        Page<SchoolEntity> emptyPage =
                new PageImpl<>(List.of(), pageable, 0);

        when(schoolRepository.findAll(pageable)).thenReturn(emptyPage);

        // Executa
        Page<SchoolResponseDTO> result = useCase.execute(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        // Verify
        verify(schoolRepository, times(1)).findAll(pageable);
        verify(schoolConverter, never()).toResponseDTO(any());
    }
}
