package com.example.projectapi.application.school.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.dto.SchoolUpdateDTO;
import com.example.projectapi.application.school.usecase.*;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Schools",
        description = "Operacoes de escolas: cadastro, listagem, atualizacao e remocao."
)
public class SchoolController {

    private final CreateSchoolUseCase createSchoolUseCase;
    private final DeleteSchoolUseCase deleteSchoolUseCase;
    private final ProfileSchoolUseCase profileSchoolUseCase;
    private final UpdateSchoolUseCase updateSchoolUseCase;
    private final ListStudentSchoolUseCase listStudentSchoolUseCase;

    @PostMapping
    @Operation(
            summary = "Cadastra uma escola.",
            description = "Cria um novo perfil de escola com usuario e endereco vinculados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escola criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "409", description = "Email ou CNPJ ja cadastrado.")
    })
    public ResponseEntity<ResponseDefault<SchoolResponseDTO>> create(
            @RequestBody @Valid
            SchoolRequestDTO request
    ){
        SchoolResponseDTO school = createSchoolUseCase.execute(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(school.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDefault<>(
                        true,
                        school,
                        "Escola criada com sucesso.",
                        List.of()
                )
        );

    }

    @GetMapping("/profile")
    @Operation(
            summary = "Perfil escola",
            description = "Retorna o perfil da escola cadastrada."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Perfil da escola obtida com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Nao autenticado."),
                    @ApiResponse(responseCode = "403", description = "Sem permissao.")
            }
    )
    public ResponseEntity<ResponseDefault<SchoolResponseDTO>> list(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser
    ){
        SchoolResponseDTO school = profileSchoolUseCase.execute(currentUser);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        school,
                        "Perfil da escola obtido com sucesso..",
                        List.of()
                )
        );
    }

    @GetMapping("/students")
    @Operation(
            summary = "Lista estudantes da própria escola.",
            description = "Lista os estudantes da escola autenticada com paginação."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista de alunos obtida com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Nao autenticado."),
                    @ApiResponse(responseCode = "403", description = "Sem permissao."),
                    @ApiResponse(responseCode = "404", description = "Escola não encontrada")
            }
    )
    public ResponseEntity<ResponseDefault<Page<StudentResponseDTO>>> listStudents(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @Min(value = 0, message = "A quantidade de pagina deve ser igual ou maior que 1") @RequestParam(defaultValue = "0") int page,
            @Min(value = 1, message = "A quantidade de elementos deve ser igual ou maior que 1") @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<StudentResponseDTO> students = listStudentSchoolUseCase.execute(currentUser, pageable);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        students,
                        "Lista de estudantes vinculados a escola obtidas com sucesso",
                        List.of()
                )
        );
    }

    @PutMapping("/me")
    @Operation(
            summary = "Atualiza a propria escola.",
            description = "Atualiza dados da escola autenticada."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<SchoolResponseDTO>> update(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid SchoolUpdateDTO updateDTO,
            @RequestHeader("If-Match") Long version
    ){

        SchoolResponseDTO updatedSchool = updateSchoolUseCase.execute(currentUser, updateDTO, version);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        updatedSchool,
                        "Escola atualizada com sucesso.",
                        List.of()
                )
        );
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Deleta a propria escola.",
            description = "Remove permanentemente o perfil da escola autenticada."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola deletada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<Void>> delete(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser
    ){
        deleteSchoolUseCase.execute(currentUser);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        null,
                        "Escola deletada com sucesso.",
                        List.of()
                )
        );
    }
}
