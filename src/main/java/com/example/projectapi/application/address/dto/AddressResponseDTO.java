package com.example.projectapi.application.address.dto;

import com.example.projectapi.domain.address.model.StateUF;

public record AddressResponseDTO(
        Long id,
        String cep,
        String street,
        String neighborhood,
        String city,
        StateUF state,
        Integer number,
        String complement,
        Long version
) {
}
