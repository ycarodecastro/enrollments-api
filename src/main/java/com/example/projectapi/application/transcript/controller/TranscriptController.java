package com.example.projectapi.application.transcript.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.transcript.dto.TranscriptResponseDTO;
import com.example.projectapi.application.transcript.usecase.GetCurrentTranscriptUseCase;
import com.example.projectapi.application.transcript.usecase.GetStudentTranscriptUseCase;
import com.example.projectapi.application.transcript.usecase.ListAllTranscriptUseCase;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/transcripts")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Transcripts",
        description = "Operacoes de boletim: criacao e listagem."
)
public class TranscriptController {

    private final ListAllTranscriptUseCase listAllTranscriptUseCase;
    private final GetCurrentTranscriptUseCase getCurrentTranscriptUseCase;
    private final GetStudentTranscriptUseCase getStudentTranscriptUseCase;

    @GetMapping
    @Operation(summary = "Lista boletins do aluno autenticado.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<Page<TranscriptResponseDTO>>> list(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TranscriptResponseDTO> transcripts = listAllTranscriptUseCase.execute(currentUser, pageable);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        transcripts,
                        "Lista de boletins obtida com sucesso.",
                        List.of()
                )
        );
    }

    @GetMapping("/current")
    @Operation(summary = "Retorna boletim atual do aluno autenticado.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<TranscriptResponseDTO>> current(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestParam(required = false) Year schoolYear
    ) {
        TranscriptResponseDTO transcript = getCurrentTranscriptUseCase.execute(currentUser, schoolYear);
        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        transcript,
                        "Boletim atual obtido com sucesso.",
                        List.of()
                )
        );
    }

    @GetMapping("/student/{idStudent}")
    @Operation(summary = "Escola consulta boletim de um aluno específico por ano.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ResponseDefault<TranscriptResponseDTO>> getStudentTranscript(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @PathVariable Long idStudent,
            @RequestParam(required = false) Year schoolYear
    ) {
        TranscriptResponseDTO transcript = getStudentTranscriptUseCase.execute(currentUser, idStudent, schoolYear);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        transcript,
                        "Boletim do aluno obtido com sucesso.",
                        List.of()
                )
        );
    }
}
