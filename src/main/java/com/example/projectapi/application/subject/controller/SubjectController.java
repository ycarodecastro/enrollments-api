package com.example.projectapi.application.subject.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.subject.dto.SubjectRequestDTO;
import com.example.projectapi.application.subject.dto.SubjectResponseDTO;
import com.example.projectapi.application.subject.usecase.CreateSubjectUseCase;
import com.example.projectapi.application.subject.usecase.ListSubjectUseCase;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Subjects",
        description = "Operacoes de materias da escola."
)
public class SubjectController {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final ListSubjectUseCase listSubjectUseCase;

    @PostMapping
    @Operation(summary = "Cria materia.")
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Materia criada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Escola nao encontrada."),
            @ApiResponse(responseCode = "409", description = "Materia duplicada para a escola.")
    })
    public ResponseEntity<ResponseDefault<SubjectResponseDTO>> create(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid SubjectRequestDTO request
    ) {
        SubjectResponseDTO subject = createSubjectUseCase.execute(currentUser, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subject.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDefault<>(true, subject, "Materia criada com sucesso.", List.of())
        );
    }

    @GetMapping
    @Operation(summary = "Lista materias da escola autenticada.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<List<SubjectResponseDTO>>> list(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser
    ) {
        List<SubjectResponseDTO> subjects = listSubjectUseCase.execute(currentUser);
        return ResponseEntity.ok(
                new ResponseDefault<>(true, subjects, "Lista de materias obtida com sucesso.", List.of())
        );
    }
}
