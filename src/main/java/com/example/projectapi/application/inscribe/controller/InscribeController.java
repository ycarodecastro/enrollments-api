package com.example.projectapi.application.inscribe.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.inscribe.dto.InscribeRequestDTO;
import com.example.projectapi.application.inscribe.dto.InscribeResponseDTO;
import com.example.projectapi.application.inscribe.dto.InscribeUpdateDTO;
import com.example.projectapi.application.inscribe.usecase.CreateInscribeUseCase;
import com.example.projectapi.application.inscribe.usecase.ListInscribeUseCase;
import com.example.projectapi.application.inscribe.usecase.UpdateInscribeUseCase;
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
@RequestMapping("/api/inscribes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Inscribes",
        description = "Operacoes de inscricoes em ofertas."
)
public class InscribeController {

    private final CreateInscribeUseCase createInscribeUseCase;
    private final ListInscribeUseCase listInscribeUseCase;
    private final UpdateInscribeUseCase updateInscribeUseCase;

    @PostMapping
    @Operation(
            summary = "Cria uma inscricao.",
            description = "Cria uma inscricao do aluno autenticado em uma oferta."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscricao criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Oferta ou aluno nao encontrado."),
            @ApiResponse(responseCode = "409", description = "Inscricao ja existente ou sem vagas disponiveis.")
    })
    public ResponseEntity<ResponseDefault<InscribeResponseDTO>> create(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid InscribeRequestDTO request
    ){
        InscribeResponseDTO inscribe = createInscribeUseCase.execute(currentUser, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(inscribe.id())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDefault<>(
                        true,
                        inscribe,
                        "Inscricao criada com sucesso.",
                        List.of()
                )
        );
    }

    @GetMapping
    @Operation(
            summary = "Lista inscricoes.",
            description = "Retorna uma lista paginada de inscricoes da escola autenticada."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscricoes obtida com sucesso."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<Page<InscribeResponseDTO>>> list(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<InscribeResponseDTO> inscribes = listInscribeUseCase.execute(currentUser, pageable);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        inscribes,
                        "Lista de inscricoes obtida com sucesso.",
                        List.of()
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza uma inscricao.",
            description = "Atualiza o status de uma inscricao da escola autenticada."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da inscricao atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Inscricao ou escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<InscribeResponseDTO>> update(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @PathVariable Long id,
            @RequestBody @Valid InscribeUpdateDTO request,
            @RequestHeader("If-Match") Long version
    ) {
        InscribeResponseDTO updatedInscribe = updateInscribeUseCase.execute(currentUser, request, id, version);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        updatedInscribe,
                        "Status da inscricao atualizado com sucesso.",
                        List.of()
                )
        );
    }
}
