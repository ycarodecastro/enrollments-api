package com.example.projectapi.application.student.dto;

import com.example.projectapi.application.address.dto.AddressResponseDTO;

import java.time.LocalDate;

public record StudentResponseDTO (
    Long id,
    String name,
    String email,
    LocalDate dateBirth,

    AddressResponseDTO address
) {}
