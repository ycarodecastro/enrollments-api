package com.example.projectapi.application.inscribe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InscribeRequestDTO(
        @Schema(description = "ID da oferta.", minimum = "1", example = "1")
        @NotNull(message = "O campo de id não pode ficar vazio.")
        @Positive
        Long offer_id
) {
}
