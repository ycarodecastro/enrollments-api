package com.example.projectapi.application.school.dto;

import com.example.projectapi.application.address.dto.AddressUpdateDTO;
import com.example.projectapi.application.user.dto.UserUpdateDTO;
import com.example.projectapi.infra.serialization.CleanupDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record SchoolUpdateDTO (
        @Schema(description = "Nome da escola.", minLength = 3, maxLength = 120, example = "Escola Exemplo")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 digitos.")
        String name,

        @Schema(description = "CNPJ da escola (14 digitos).", pattern = "^[0-9]{14}$", minLength = 14, maxLength = 14, example = "12345678000199")
        @Size(min = 14, max = 14, message = "O campo de cnpj deve conter exatamente 14 digitos.")
        @CNPJ(message = "CNPJ inválido.")
        @JsonDeserialize(using = CleanupDeserializer.class)
        String cnpj,

        @Schema(description = "Dados do usuario da escola.")
        @Valid
        UserUpdateDTO user,

        @Schema(description = "Endereco da escola.")
        @Valid
        AddressUpdateDTO address,

        @NotNull
        Long version
) {}
