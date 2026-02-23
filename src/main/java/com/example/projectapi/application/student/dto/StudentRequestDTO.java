package com.example.projectapi.application.student.dto;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.infra.serialization.CleanupDeserializer;
import com.example.projectapi.infra.validation.rg.Rg;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record StudentRequestDTO (
    @Schema(description = "Nome completo do aluno.", minLength = 3, maxLength = 130, example = "Joao da Silva")
    @NotBlank(message = "O campo de nome não pode ficar vazio.")
    @Size(min = 3, max = 130, message = "O campo de nome deve conter entre 3 a 120 caracteres.")
    String name,

    @Schema(description = "RG do aluno (somente numeros).")
    @NotBlank(message = "O campo de rg não pode ficar vazio.")
    @Rg(message = "Rg inválido.")
    @JsonDeserialize(using = CleanupDeserializer.class)
    String rg,

    @Schema(description = "CPF do aluno (11 digitos).", pattern = "^[0-9]{11}$", example = "12345678901")
    @NotBlank(message = "O campo de cpf não pode ficar vazio.")
    @CPF(message = "CPF inválido.")
    @JsonDeserialize(using = CleanupDeserializer.class)
    String cpf,

    @Schema(description = "Data de nascimento.", type = "string", format = "date", example = "2005-05-10")
    @NotNull(message = "O campo de data de nascimento não pode ficar vazio.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    LocalDate dateBirth,

    @Schema(description = "Dados do usuario do aluno.")
    @NotNull(message = "O usuario é inválido.")
    @Valid
    UserRequestDTO user,

    @Schema(description = "Endereco do aluno.")
    @NotNull(message = "O endereço é inválido.")
    @Valid
    AddressRequestDTO address,

    Long version

) {}
