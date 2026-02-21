package com.example.projectapi.application.address.dto;

import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.infra.serialization.CleanupDeserializer;
import com.example.projectapi.infra.validation.cep.Cep;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddressRequestDTO(
        @Schema(description = "CEP somente numeros (8 digitos).", pattern = "^[0-9]{8}$", example = "600000-00")
        @NotBlank(message = "O campo de cep é obrigatorio.")
        @Cep
        @JsonDeserialize(using = CleanupDeserializer.class)
        String cep,

        @Schema(description = "Rua e logradouro.", example = "Rua das Flores")
        @NotBlank(message = "O campo de rua é obrigatorio.")
        String street,

        @Schema(description = "Bairro.", example = "Centro")
        @NotBlank(message = "O campo de bairro é obrigatorio.")
        String neighborhood,

        @Schema(description = "Cidade.", example = "Sao Paulo")
        @NotBlank(message = "O campo de cidade é obrigatorio.")
        String city,

        @Schema(description = "UF do estado.", example = "SP")
        @NotNull(message = "O campo de estado é obrigatorio.")
        StateUF state,

        @Schema(description = "Numero do endereco.", minimum = "1", example = "100")
        @NotNull(message = "O campo de número é obrigatorio.")
        @Positive
        Integer number,

        @Schema(description = "Complemento (opcional).", example = "Apto 12")
        String complement
) {
}
