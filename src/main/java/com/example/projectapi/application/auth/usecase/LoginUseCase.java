package com.example.projectapi.application.auth.usecase;

import com.example.projectapi.application.auth.dto.RefreshToken.RefreshTokenResponseDTO;
import com.example.projectapi.application.auth.dto.Token.LoginRequestDTO;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.redis.RedisRateLimitProperties;
import com.example.projectapi.infra.redis.RedisRateLimitService;
import com.example.projectapi.util.RequestIpUtil;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RedisRateLimitService rateLimitService;
    private final RedisRateLimitProperties rateLimitProperties;
    private final MeterRegistry meterRegistry;

    public RefreshTokenResponseDTO execute(LoginRequestDTO dto, HttpServletRequest request) {
        String email = dto.email();
        String ip = RequestIpUtil.extractClientIp(request);

        String userKey = "login:user:" + email;
        String ipKey = "login:ip:" + ip;

        if (rateLimitService.isBlocked(userKey, rateLimitProperties.getUserMaxAttempts())
                || rateLimitService.isBlocked(ipKey, rateLimitProperties.getIpMaxAttempts())) {
            meterRegistry.counter("auth.login.blocked").increment();
            log.warn("Login bloqueado por rate limit para email {}.", email);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many login attempts");
        }

        Duration blockDuration = Duration.ofMinutes(rateLimitProperties.getBlockDurationMinutes());

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    rateLimitService.incrementAttempts(userKey, blockDuration);
                    rateLimitService.incrementAttempts(ipKey, blockDuration);
                    meterRegistry.counter("auth.login.failed", "reason", "invalid_credentials").increment();
                    log.warn("Falha de login por credenciais invalidas para email {}.", email);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
                });

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            rateLimitService.incrementAttempts(userKey, blockDuration);
            rateLimitService.incrementAttempts(ipKey, blockDuration);
            meterRegistry.counter("auth.login.failed", "reason", "invalid_credentials").increment();
            log.warn("Falha de login por credenciais invalidas para email {}.", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        if (!user.isActive()) {
            meterRegistry.counter("auth.login.failed", "reason", "inactive_user").increment();
            log.warn("Falha de login para usuario inativo. userId={}", user.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sua conta esta inativa.");
        }

        rateLimitService.reset(userKey);
        rateLimitService.reset(ipKey);
        meterRegistry.counter("auth.login.success").increment();
        log.info("Login realizado com sucesso. userId={}", user.getId());
        return refreshTokenUseCase.createSession(user);
    }
}
