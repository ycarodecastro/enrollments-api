package com.example.projectapi.application.auth.dto.Token;

public record LoginResponseDTO(
        String token,
        String type,
        Long userId,
        String role

) {}
