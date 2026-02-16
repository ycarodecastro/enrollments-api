package com.example.projectapi.application.grade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeRequestDTO(
        @Schema(description = "ID da materia", example = "1")
        @NotNull(message = "O campo de materia nao pode ficar vazio.")
        Long subjectId,

        @Schema(description = "Valor da nota", example = "0.0 a 10.0")
        @NotNull(message = "O campo de nota nao pode ficar vazio.")
        @DecimalMax("10.0")
        @DecimalMin("0.0")
        Double value,

        @Schema(description = "Periodo da nota", example = "1 bimestre")
        @NotBlank(message = "O campo de periodo nao pode ficar vazio.")
        String period
) {}
