package com.example.projectapi.application.student.dto;

import com.example.projectapi.application.address.dto.AddressUpdateDTO;
import com.example.projectapi.application.user.dto.UserUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentUpdateDTO (
        @Schema(description = "Nome completo do aluno.", minLength = 3, maxLength = 130, example = "Joao da Silva")
        @Size(min = 3, max = 130, message = "O campo de nome deve conter entre 3 a 120 caracteres.")
        String name,

        @Schema(description = "Data de nascimento.", type = "string", format = "date", example = "2005-05-10")
        @Past(message = "A data de nascimento deve ser uma data no passado.")
        LocalDate dateBirth,

        @Schema(description = "Dados do usuario do aluno.")
        @Valid
        UserUpdateDTO user,

        @Schema(description = "Endereco do aluno.")
        @Valid
        AddressUpdateDTO address
) {}
