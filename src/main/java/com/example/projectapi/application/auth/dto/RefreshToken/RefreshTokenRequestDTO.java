package com.example.projectapi.application.auth.dto.RefreshToken;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank
        @Schema(description = "O UUID do Refresh Token armazenado no banco",
                example = "550e8400-e29b-41d4-a716-446655440000")
        String refreshToken
) {}
