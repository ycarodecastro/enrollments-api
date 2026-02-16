package com.example.projectapi.application.school.dto;

import com.example.projectapi.application.address.dto.AddressResponseDTO;

public record SchoolResponseDTO(
   Long id,
   String name,
   String email,

   AddressResponseDTO address
) {}
