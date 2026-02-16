package com.example.projectapi.application.grade.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.grade.dto.GradeRequestDTO;
import com.example.projectapi.application.grade.dto.GradeResponseDTO;
import com.example.projectapi.application.grade.dto.GradeUpdateDTO;
import com.example.projectapi.application.grade.usecase.CreateGradeUseCase;
import com.example.projectapi.application.grade.usecase.DeleteGradeUseCase;
import com.example.projectapi.application.grade.usecase.UpdateGradeUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transcripts/{transcriptId}/grades")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Grades",
        description = "Operacoes de notas dentro do boletim."
)
public class GradeController {

    private final CreateGradeUseCase createGradeUseCase;
    private final UpdateGradeUseCase updateGradeUseCase;
    private final DeleteGradeUseCase deleteGradeUseCase;

    @PostMapping
    @Operation(summary = "Lanca nota em um boletim.")
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nota criada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Boletim ou materia nao encontrados."),
            @ApiResponse(responseCode = "409", description = "Nota duplicada para materia/periodo.")
    })
    public ResponseEntity<ResponseDefault<GradeResponseDTO>> create(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @PathVariable Long transcriptId,
            @RequestBody @Valid GradeRequestDTO request
    ) {
        GradeResponseDTO grade = createGradeUseCase.execute(currentUser, transcriptId, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{gradeId}")
                .buildAndExpand(grade.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDefault<>(
                        true,
                        grade,
                        "Nota criada com sucesso.",
                        List.of()
                )
        );
    }

    @PutMapping("/{gradeId}")
    @Operation(summary = "Atualiza nota de um boletim.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<GradeResponseDTO>> update(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @PathVariable Long transcriptId,
            @PathVariable Long gradeId,
            @RequestBody @Valid GradeUpdateDTO request
    ) {
        GradeResponseDTO grade = updateGradeUseCase.execute(currentUser, transcriptId, gradeId, request);
        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        grade,
                        "Nota atualizada com sucesso.",
                        List.of()
                )
        );
    }

    @DeleteMapping("/{gradeId}")
    @Operation(summary = "Remove nota de um boletim.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<Void>> delete(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @PathVariable Long transcriptId,
            @PathVariable Long gradeId
    ) {
        deleteGradeUseCase.execute(currentUser, transcriptId, gradeId);
        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        null,
                        "Nota removida com sucesso.", List.of()
                )
        );
    }
}
