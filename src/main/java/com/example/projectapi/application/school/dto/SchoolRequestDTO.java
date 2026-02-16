package com.example.projectapi.application.school.dto;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.infra.serialization.CleanupDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record SchoolRequestDTO(

        @Schema(description = "Nome da escola.", minLength = 3, maxLength = 120, example = "Escola Exemplo")
        @NotBlank(message = "O campo de nome não pode ficar vazio.")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 digitos.")
        String name,

        @Schema(description = "CNPJ da escola (14 digitos).", pattern = "^[0-9]{14}$", example = "12345678000199")
        @NotBlank(message = "O campo de cnpj não pode ficar vazio.")
        @CNPJ(message = "CNPJ inválido.")
        @JsonDeserialize(using = CleanupDeserializer.class)
        String cnpj,

        @Schema(description = "Dados do usuario da escola.")
        @NotNull
        @Valid
        UserRequestDTO user,

        @Schema(description = "Endereco da escola.")
        @NotNull
        @Valid
        AddressRequestDTO address
) {}
