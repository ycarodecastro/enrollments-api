package com.example.projectapi.application.auth.usecase;

import com.example.projectapi.domain.refreshToken.repository.RefreshTokenRepository;
import com.example.projectapi.infra.redis.RedisJwtService;
import com.example.projectapi.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutUseCase {
    private final JwtUtil jwtUtil;
    private final RedisJwtService redisJwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(String token) {
        try {
            String jti = jwtUtil.getJti(token);
            Long userId = jwtUtil.getUserId(token);
            Date expiration = jwtUtil.getExpiration(token);

            long ttl = expiration.getTime() - System.currentTimeMillis();
            if (ttl > 0) {
                redisJwtService.blacklist(jti, ttl);
            }

            refreshTokenRepository.deleteByUserId(userId);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido.");
        }
    }
}
