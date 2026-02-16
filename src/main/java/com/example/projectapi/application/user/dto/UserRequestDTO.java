package com.example.projectapi.application.user.dto;

import com.example.projectapi.infra.validation.password.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @Schema(description = "Email do usuario.", example = "aluno@example.com")
        @NotBlank(message = "O campo de email não pode ficar vazio.")
        @Email(message = "O email é inválido.")
        String email,

        @Schema(
                description = "Senha do usuario. Minimo 8 caracteres, com pelo menos 1 letra maiuscula e 1 numero.",
                minLength = 8,
                example = "Senha123"
        )
        @NotBlank(message = "O campo de senha não pode ficar vazio.")
        @Password
        String password
) {}
