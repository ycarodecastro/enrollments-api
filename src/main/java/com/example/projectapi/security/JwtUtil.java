package com.example.projectapi.security;

import com.example.projectapi.domain.user.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationMinutes}")
    private Long expiration;

    public String generationToken(Long userId, UserRole role){
        return Jwts.builder()
                .setSubject(userId.toString())
                .addClaims(Map.of("role",  role.toString()))
                .setIssuedAt(new Date())
                .setId(java.util.UUID.randomUUID().toString())
                // expiration está em minutos
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * 60_000)))
                .signWith(getKey(), SignatureAlgorithm.HS256) // assinatura
                .compact();
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // token inválido
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    public String getJti(String token){
        return getClaims(token).getId();
    }

    // Pegar userId do token
    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    // Pegar role do token
    public String getRole(String token) {
        return (String) getClaims(token).get("role");
    }

    private Key getKey() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("JWT_SECRET_KEY nao foi configurada.");
        }

        byte[] rawKey = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(rawKey);
    }
}
