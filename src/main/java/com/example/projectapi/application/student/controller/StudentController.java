package com.example.projectapi.application.student.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.student.dto.StudentRequestDTO;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.application.student.dto.StudentUpdateDTO;
import com.example.projectapi.application.student.usecase.CreateStudentUseCase;
import com.example.projectapi.application.student.usecase.DeleteStudentUseCase;
import com.example.projectapi.application.student.usecase.ProfileStudentUseCase;
import com.example.projectapi.application.student.usecase.UpdateStudentUseCase;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Students",
        description = "Operacoes de alunos: cadastro, listagem, atualizacao e remocao."
)
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final ProfileStudentUseCase profileStudentUseCase;

    @PostMapping
    @Operation(
            summary = "Cadastra um novo aluno.",
            description = "Cria um novo perfil de aluno com usuario e endereco vinculados. Email, CPF e RG devem ser unicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "409", description = "Email, CPF ou RG ja cadastrado.")
    })
    public ResponseEntity<ResponseDefault<StudentResponseDTO>> create(@RequestBody @Valid StudentRequestDTO request){
        StudentResponseDTO student = createStudentUseCase.execute(request);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        student,
                        "Aluno criado com sucesso.",
                        List.of()
                )
        );
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Perfil estudante.",
            description = "Retorna o perfil do aluno cadastrado."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Perfil do aluno obtida com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Nao autenticado."),
                    @ApiResponse(responseCode = "403", description = "Sem permissao.")
            }
    )
    public ResponseEntity<ResponseDefault<StudentResponseDTO>> profile(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser
    ) {
        StudentResponseDTO student = profileStudentUseCase.execute(currentUser);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        student,
                        "Perfil do estudante obtido com sucesso.",
                        List.of()
                )
        );
    }

    @PutMapping("/me")
    @Operation(
            summary = "Atualiza o proprio aluno.",
            description = "Atualiza dados pessoais, usuario e endereco do aluno autenticado."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Aluno nao encontrado.")
    })
    public ResponseEntity<ResponseDefault<StudentResponseDTO>> update(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid StudentUpdateDTO updateDTO
    ) {
        StudentResponseDTO UpdatedStudent = updateStudentUseCase.execute(currentUser, updateDTO);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        UpdatedStudent,
                        "Aluno atualizado com sucesso.",
                        List.of()
                )
        );
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Deleta o proprio aluno.",
            description = "Remove permanentemente o perfil do aluno autenticado, incluindo usuario e endereco associados."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno deletado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Aluno nao encontrado.")
    })
    public ResponseEntity<ResponseDefault<Void>> delete(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser
    ) {
        deleteStudentUseCase.execute(currentUser);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        null,
                        "Aluno deletado com sucesso.",
                        List.of()
                )
        );
    }
}
