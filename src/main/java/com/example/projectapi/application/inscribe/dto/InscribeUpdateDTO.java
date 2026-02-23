package com.example.projectapi.application.inscribe.dto;

import com.example.projectapi.domain.inscribe.model.InscribeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record InscribeUpdateDTO(
        @Schema(description = "Novo status da inscricao.", example = "ACEITO")
        InscribeStatus status,

        @NotNull
        Long version
) {}
