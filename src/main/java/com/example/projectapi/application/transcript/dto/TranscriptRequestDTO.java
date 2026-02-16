package com.example.projectapi.application.transcript.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Year;

public record TranscriptRequestDTO(
   @Schema(description = "Ano do boletim", example = "2025")
   @NotNull(message = "O campo de ano escolar nao pode ficar vazio.")
   @PastOrPresent(message = "O ano letivo não pode estar no futuro.")
   Year schoolYear
) {}
