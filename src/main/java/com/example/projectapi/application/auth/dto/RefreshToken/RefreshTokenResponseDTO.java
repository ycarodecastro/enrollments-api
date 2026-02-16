package com.example.projectapi.application.auth.dto.RefreshToken;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponseDTO {
    @Schema(description = "Token de acesso (JWT) - Expira em 15 min")
    private String accessToken;

    @Schema(description = "Token de renovação (UUID) - Expira em 14 dias")
    private String refreshToken;
}
