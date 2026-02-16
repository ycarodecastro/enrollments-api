package com.example.projectapi.security;

import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserEntity get() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao autenticado.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity user) {
            return user;
        }

        if (principal instanceof Long userId) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado."));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao autenticado.");
    }
}
