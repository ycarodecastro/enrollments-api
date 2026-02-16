package com.example.projectapi.application.inscribe.dto;

import com.example.projectapi.domain.inscribe.model.InscribeStatus;

public record InscribeResponseDTO(
        Long id,
        Long offer_id,
        String studentName,
        // necessario quando adicionar o sistema de notas -> String studentRegistry,
        InscribeStatus status
) {
}
