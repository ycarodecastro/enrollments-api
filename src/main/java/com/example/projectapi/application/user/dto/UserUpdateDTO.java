package com.example.projectapi.application.user.dto;

import com.example.projectapi.infra.validation.password.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO (
    @Schema(description = "Email do usuario.", example = "aluno@example.com")
    @Email(message = "Email inválido.")
    String email,

    @Schema(description = "Senha do usuario. Minimo 6 caracteres.", minLength = 6, example = "Senha123")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Password
    String password
) {}
