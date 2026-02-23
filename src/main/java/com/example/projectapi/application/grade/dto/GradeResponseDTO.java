package com.example.projectapi.application.grade.dto;

public record GradeResponseDTO(
        Long id,
        String subjectName,
        Double value,
        String period,
        Long version
) {}
