package com.example.projectapi.application.school.usecase;

import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final SchoolConverter schoolConverter;

    public SchoolResponseDTO execute(UserEntity currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            log.error(
                    "Tentativa de acesso ao perfil negada. Sem usuario autenticado."
            );
            throw new SchoolNotFoundException();
        }

        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn(
                            "Escola não encontrado para o User ID: {}",
                            currentUser.getId()
                    );
                    return new SchoolNotFoundException();
                });

        log.info(
                "Perfil do estudante {} recuperado com sucesso.",
                school.getName()
        );
        return schoolConverter.toResponseDTO(school);
    }
}
