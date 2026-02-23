package com.example.projectapi.application.address.dto;

import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.infra.serialization.CleanupDeserializer;
import com.example.projectapi.infra.validation.cep.Cep;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressUpdateDTO(
        @Schema(description = "CEP somente numeros (8 digitos).", pattern = "^[0-9]{8}$", minLength = 8, maxLength = 8, example = "01001000")
        @Size(min = 8, max = 8, message = "O campo de cep deve conter exatamente 8 digitos.")
        @Cep
        @JsonDeserialize(using = CleanupDeserializer.class)
        String cep,

        @Schema(description = "Rua e logradouro.", example = "Rua das Flores")
        String street,

        @Schema(description = "Bairro.", example = "Centro")
        String neighborhood,

        @Schema(description = "Cidade.", example = "Sao Paulo")
        String city,

        @Schema(description = "UF do estado.", example = "SP")
        StateUF state,

        @Schema(description = "Numero do endereco.", minimum = "1", example = "100")
        @Positive
        Integer number,

        @Schema(description = "Complemento (opcional).", example = "Apto 12")
        String complement,

        @NotNull
        Long version
) {
}
