package com.example.projectapi.application.offer.dto;

import java.time.LocalDate;
import java.time.Year;

public record OfferResponseDTO(
    Long id,
    String schoolName,
    String grade,
    String classGroup,
    String shift,
    Year schoolYear,
    Integer availableSeats,
    LocalDate startDate,
    LocalDate endDate,

    Integer totalInscribed,
    Long version
) {}
