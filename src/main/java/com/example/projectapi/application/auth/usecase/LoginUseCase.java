package com.example.projectapi.application.auth.usecase;

import com.example.projectapi.application.auth.dto.Token.LoginRequestDTO;
import com.example.projectapi.application.auth.dto.RefreshToken.RefreshTokenResponseDTO;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.redis.RedisRateLimitProperties;
import com.example.projectapi.infra.redis.RedisRateLimitService;
import com.example.projectapi.util.RequestIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RedisRateLimitService rateLimitService;
    private final RedisRateLimitProperties rateLimitProperties;

    public RefreshTokenResponseDTO execute(LoginRequestDTO dto, HttpServletRequest request) {
        String email = dto.email();
        String ip = RequestIpUtil.extractClientIp(request);

        String userKey = "login:user:" + email;
        String ipKey = "login:ip:" + ip;

        if (rateLimitService.isBlocked(userKey, rateLimitProperties.getUserMaxAttempts())
                || rateLimitService.isBlocked(ipKey, rateLimitProperties.getIpMaxAttempts())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many login attempts");
        }

        Duration blockDuration = Duration.ofMinutes(rateLimitProperties.getBlockDurationMinutes());

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    rateLimitService.incrementAttempts(userKey, blockDuration);
                    rateLimitService.incrementAttempts(ipKey, blockDuration);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
                });

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            rateLimitService.incrementAttempts(userKey, blockDuration);
            rateLimitService.incrementAttempts(ipKey, blockDuration);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sua conta está inativa.");
        }

        rateLimitService.reset(userKey);
        rateLimitService.reset(ipKey);
        return refreshTokenUseCase.createSession(user);
    }
}
