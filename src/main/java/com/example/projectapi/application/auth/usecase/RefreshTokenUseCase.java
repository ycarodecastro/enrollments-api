package com.example.projectapi.application.auth.usecase;

import com.example.projectapi.application.auth.dto.RefreshToken.RefreshTokenResponseDTO;
import com.example.projectapi.domain.refreshToken.model.RefreshTokenEntity;
import com.example.projectapi.domain.refreshToken.repository.RefreshTokenRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.security.JwtUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final MeterRegistry meterRegistry;

    @Value("${jwt.refresh.expiration}")
    private Long expireTimeMs;

    public RefreshTokenResponseDTO createSession(UserEntity user) {
        String accessToken = jwtUtil.generationToken(user.getId(), user.getRole());
        RefreshTokenEntity refreshToken = createRefreshToken(user);
        return new RefreshTokenResponseDTO(accessToken, refreshToken.getToken());
    }

    public RefreshTokenResponseDTO refresh(String requestRefreshToken) {
        RefreshTokenEntity currentToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> {
                    meterRegistry.counter("auth.refresh.failed", "reason", "invalid_token").increment();
                    log.warn("Falha ao renovar sessao. Refresh token invalido.");
                    return new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "Refresh token invalido. Faca login novamente."
                    );
                });

        verifyNotExpired(currentToken);

        UserEntity user = currentToken.getUser();

        // Rotacao de refresh token: token antigo deixa de ser valido apos uso.
        refreshTokenRepository.delete(currentToken);

        String newAccessToken = jwtUtil.generationToken(user.getId(), user.getRole());
        RefreshTokenEntity newRefreshToken = createRefreshToken(user);

        meterRegistry.counter("auth.refresh.success").increment();
        log.info("Sessao renovada com sucesso. userId={}", user.getId());
        return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken.getToken());
    }

    private RefreshTokenEntity createRefreshToken(UserEntity user) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(expireTimeMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    private void verifyNotExpired(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            meterRegistry.counter("auth.refresh.failed", "reason", "expired_token").increment();
            log.warn("Falha ao renovar sessao. Refresh token expirado para userId={}", token.getUser().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Refresh token expirado. Faca login novamente."
            );
        }
    }
}
