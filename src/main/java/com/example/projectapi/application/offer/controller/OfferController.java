package com.example.projectapi.application.offer.controller;

import com.example.projectapi.application.common.ResponseDefault;
import com.example.projectapi.application.offer.dto.OfferRequestDTO;
import com.example.projectapi.application.offer.dto.OfferResponseDTO;
import com.example.projectapi.application.offer.dto.OfferUpdateDTO;
import com.example.projectapi.application.offer.usecase.CreateOfferUseCase;
import com.example.projectapi.application.offer.usecase.ListOfferUseCase;
import com.example.projectapi.application.offer.usecase.UpdateOfferUseCase;
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

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Offers",
        description = "Operacoes de ofertas: cadastro, listagem e atualizacao."
)
public class OfferController {

    private final CreateOfferUseCase createOfferUseCase;
    private final ListOfferUseCase listOfferUseCase;
    private final UpdateOfferUseCase updateOfferUseCase;

    @PostMapping
    @Operation(
            summary = "Cria uma oferta.",
            description = "Cria uma nova oferta vinculada ao usuario autenticado."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<OfferResponseDTO>> create(
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid OfferRequestDTO request
    ) {
        OfferResponseDTO offer = createOfferUseCase.execute(currentUser, request);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        offer,
                        "Oferta criada com sucesso.",
                        List.of()
                )
        );

    }

    @GetMapping
    @Operation(
            summary = "Lista ofertas.",
            description = "Retorna uma lista paginada de ofertas cadastradas."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista de ofertas obtida com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Nao autenticado."),
                    @ApiResponse(responseCode = "403", description = "Sem permissao.")
            }
    )
    public ResponseEntity<ResponseDefault<Page<OfferResponseDTO>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<OfferResponseDTO> offers = listOfferUseCase.execute(pageable);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        offers,
                        "Lista de ofertas obtida com sucesso.",
                        List.of()
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza uma oferta.",
            description = "Atualiza os dados de uma oferta do usuario autenticado."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados invalidos enviados pelo cliente."),
            @ApiResponse(responseCode = "401", description = "Nao autenticado."),
            @ApiResponse(responseCode = "403", description = "Sem permissao."),
            @ApiResponse(responseCode = "404", description = "Oferta ou escola nao encontrada.")
    })
    public ResponseEntity<ResponseDefault<OfferResponseDTO>> update(
            @PathVariable Long id,
            @Parameter(hidden = true) @CurrentUser UserEntity currentUser,
            @RequestBody @Valid OfferUpdateDTO dto

    ){
        OfferResponseDTO updatedOffer = updateOfferUseCase.execute(currentUser, dto, id);

        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        updatedOffer,
                        "Oferta atualizada com sucesso.",
                        List.of()
                )
        );
    }
}
