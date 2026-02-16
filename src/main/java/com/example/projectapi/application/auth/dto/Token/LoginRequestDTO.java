package com.example.projectapi.application.auth.dto.Token;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(description = "Email do usuario.", example = "aluno@example.com")
        @NotBlank(message = "O campo de email não pode ficar vazio.")
        @Email(message = "O email é inválido.")
        String email,

        @Schema(description = "Senha do usuario.", example = "Senha123")
        @NotBlank(message = "O campo de senha não pode ficar vazio.")
        String password
) {}
