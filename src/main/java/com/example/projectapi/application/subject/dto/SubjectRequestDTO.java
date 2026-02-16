package com.example.projectapi.application.subject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SubjectRequestDTO(
        @Schema(description = "Nome da matéria", example = "Biologia")
        @NotBlank(message = "O campo de nome não pode ficar vazio.")
        String name
) {}
