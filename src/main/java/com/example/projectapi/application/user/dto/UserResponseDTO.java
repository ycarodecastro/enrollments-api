package com.example.projectapi.application.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserResponseDTO (
    Long id,
    String email,
    String role,
    boolean active,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    Long version
) {}
