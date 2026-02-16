package com.example.projectapi.application.school.usecase;

import com.example.projectapi.application.address.converter.AddressConverter;
import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.dto.SchoolUpdateDTO;
import com.example.projectapi.application.user.converter.UserConverter;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final SchoolConverter schoolConverter;
    private final AddressConverter addressConverter;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SchoolResponseDTO execute(
            UserEntity currentUser,
            SchoolUpdateDTO dto
    ) {

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar escola. Escola não encontrada.");
                    return new SchoolNotFoundException();
                });

        schoolConverter.updateEntityFromDTO(dto, school);

        if (dto.address() != null) {
            addressConverter.updateEntityFromDTO(dto.address(), school.getAddress());
        }
        if (dto.user() != null) {

            if (dto.user().password() != null && !dto.user().password().isBlank()) {
                school.getUser().setPassword(passwordEncoder.encode(dto.user().password()));
            }

            userConverter.updateEntityFromDTO(dto.user(), school.getUser());
        }

        SchoolEntity updatedSchool = schoolRepository.save(school);

        log.info("Atualização de escola concluída com sucesso.");
        return schoolConverter.toResponseDTO(updatedSchool);
    }
}
