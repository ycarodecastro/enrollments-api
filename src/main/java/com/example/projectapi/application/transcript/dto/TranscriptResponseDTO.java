package com.example.projectapi.application.transcript.dto;

import com.example.projectapi.application.grade.dto.GradeResponseDTO;

import java.time.Year;
import java.util.List;

public record TranscriptResponseDTO(
        Long id,
        Year schoolYear,
        String studentName,
        String studentCpf,
        String studentRg,
        String schoolName,
        List<GradeResponseDTO> grades
) {}
