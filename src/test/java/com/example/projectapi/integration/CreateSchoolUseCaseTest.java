package com.example.projectapi.integration;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.usecase.CreateSchoolUseCase;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // usa application-test.yml
class CreateSchoolUseCaseTest {

    @Autowired
    private CreateSchoolUseCase useCase;

    @Autowired
    private SchoolRepository schoolRepository;

    @Test
    void shouldCreateSchoolInDatabase() {

        // Arrange (dados reais)
        SchoolRequestDTO dto = new SchoolRequestDTO(
                "Patronato",
                "12345678900011",
                new UserRequestDTO("real@gmail.com", "123456"),
                new AddressRequestDTO(
                        "12345678",
                        "Rua A",
                        "Centro",
                        "São Paulo",
                        StateUF.SP,
                        100,
                        ""
                )
        );

        // Act (executa real)
        SchoolResponseDTO response = useCase.execute(dto);

        // Assert (confirma no banco)
        assertNotNull(response);
        assertNotNull(response.id());

        assertTrue(
                schoolRepository.existsById(response.id())
        );

        System.out.println("Escola salva no banco com ID: " + response.id());
        System.out.println("Escola salva no banco com nome: "  + response.name());;
    }
}
