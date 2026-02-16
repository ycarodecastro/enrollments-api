package com.example.projectapi.domain.refreshToken.repository;

import com.example.projectapi.domain.refreshToken.model.RefreshTokenEntity;
import com.example.projectapi.domain.user.model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUser(UserEntity user);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
