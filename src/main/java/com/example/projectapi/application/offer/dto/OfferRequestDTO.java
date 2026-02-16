package com.example.projectapi.application.offer.dto;

import com.example.projectapi.infra.validation.date.DateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Year;

@DateRange(startField = "startDate", endField = "endDate", message = "Datas inconsistentes!")
public record OfferRequestDTO (
        @Schema(description = "Serie/ano de ensino.", example = "6o ano")
        @NotBlank(message = "O campo de série é obrigatório.")
        String grade,

        @Schema(description = "Turma.", example = "A")
        @NotBlank(message = "O campo de turma é obrigatório.")
        String classGroup,

        @Schema(description = "Turno.", example = "Manha")
        @NotBlank(message = "O campo de turno é obrigatório.")
        String shift,

        @Schema(description = "Ano letivo.", example = "2025")
        @NotNull(message = "O campo de ano escolar é obrigatório.")
        @PastOrPresent(message = "O ano letivo não pode estar no futuro.")
        Year schoolYear,

        @Schema(description = "Numero de vagas disponiveis.", minimum = "1", example = "30")
        @NotNull(message = "O campo de vagas disponíveis é obrigatório.")
        @Positive(message = "O número de vagas deve ser maior que zero.")
        Integer availableSeats,

        @Schema(description = "Data de inicio.", type = "string", format = "date", example = "2025-01-10")
        @NotNull(message = "O campo de data de início é obrigatório.")
        @FutureOrPresent(message = "A data de início não pode ser no passado.")
        LocalDate startDate,

        @Schema(description = "Data de fim (igual ou posterior ao inicio).", type = "string", format = "date", example = "2025-12-10")
        @NotNull(message = "O campo de data de finalização é obrigatório.")
        LocalDate endDate
) {}
