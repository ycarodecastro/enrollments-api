package com.example.projectapi.application.offer.dto;

import com.example.projectapi.infra.validation.date.DateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.Year;

@DateRange(startField = "startDate", endField = "endDate", message = "Datas inconsistentes!")
public record OfferUpdateDTO(
        @Schema(description = "Serie/ano de ensino.", example = "6o ano")
        String grade,

        @Schema(description = "Turma.", example = "A")
        String classGroup,

        @Schema(description = "Turno.", example = "Manha")
        String shift,

        @Schema(description = "Ano letivo.", example = "2025")
        Year schoolYear,

        @Schema(description = "Numero de vagas disponiveis.", minimum = "1", example = "30")
        @Positive(message = "O número de vagas deve ser maior que zero.")
        Integer availableSeats,

        @Schema(description = "Data de inicio.", type = "string", format = "date", example = "2025-01-10")
        @FutureOrPresent(message = "A data de início não pode ser no passado.")
        LocalDate startDate,

        @Schema(description = "Data de fim (igual ou posterior ao inicio).", type = "string", format = "date", example = "2025-12-10")
        LocalDate endDate,

        @NotNull
        Long version
) {}
