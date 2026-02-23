package com.example.projectapi.application.grade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record GradeUpdateDTO(
        @Schema(description = "Valor da nota", example = "0.0 á 10.0")
        @DecimalMax("10.0")
        @DecimalMin("0.0")
        Double value,

        @Schema(description = "Periodo da nota", example = "1° bimestre")
        String period,

        @NotNull
        Long version
) {}
