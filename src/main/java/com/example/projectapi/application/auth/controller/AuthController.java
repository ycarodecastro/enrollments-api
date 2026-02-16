package com.example.projectapi.application.auth.controller;

import com.example.projectapi.application.auth.dto.Token.LoginRequestDTO;
import com.example.projectapi.application.auth.dto.RefreshToken.RefreshTokenRequestDTO;
import com.example.projectapi.application.auth.usecase.LoginUseCase;
import com.example.projectapi.application.auth.usecase.LogoutUseCase;
import com.example.projectapi.application.auth.usecase.RefreshTokenUseCase;
import com.example.projectapi.application.common.ResponseDefault;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Auth",
        description = "Operações de autenticação e gestão de sessões (Refresh Token)."
)
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase; // Novo UseCase
    private final LogoutUseCase logoutUseCase;

    @PostMapping(
            value = "/login",
            consumes = "application/json",
            produces = "application/json"
    )
    @Operation(
            summary = "Autentica usuário.",
            description = "Valida credenciais e retorna um token de acesso e um refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    })
    // Alterado de String para Object ou um DTO de LoginResponse que contenha os 2 tokens
    public ResponseEntity<ResponseDefault<?>> login(
            @RequestBody @Valid LoginRequestDTO dto,
            HttpServletRequest request
    ) {
        var authResponse = loginUseCase.execute(dto, request);
        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        authResponse,
                        "Login feito com sucesso.",
                        List.of()
                )
        );
    }

    @PostMapping(
            value = "/refresh",
            consumes = "application/json",
            produces = "application/json"
    )
    @Operation(
            summary = "Renova o acesso.",
            description = "Envia um refresh token válido para obter um novo token de acesso (JWT)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Refresh token expirado ou inválido.")
    })
    public ResponseEntity<ResponseDefault<?>> refresh(
            @RequestBody @Valid RefreshTokenRequestDTO dto
    ) {
        var newAuthResponse = refreshTokenUseCase.refresh(dto.refreshToken());
        return ResponseEntity.ok(
                new ResponseDefault<>(
                        true,
                        newAuthResponse,
                        "Token renovado com sucesso.",
                        List.of()
                )
        );
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Encerra a sessão.",
            description = "Invalida o token atual e remove o Refresh Token do banco de dados.",
            security = @SecurityRequirement(name = "bearer-key") // Ativa o cadeado no Swagger
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.")
    })
    public ResponseEntity<Void> logout(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader
    ) {

        logoutUseCase.execute(authHeader.replace("Bearer ", ""));

        return ResponseEntity.noContent().build();
    }

}
