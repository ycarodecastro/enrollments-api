package com.example.projectapi.application.school.usecase;

import com.example.projectapi.application.address.converter.AddressConverter;
import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.dto.SchoolUpdateDTO;
import com.example.projectapi.application.user.converter.UserConverter;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.exception.school.SchoolCnpjAlreadyExistsException;
import com.example.projectapi.infra.exception.school.SchoolEmailAlreadyExistsException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
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
    private final UserRepository userRepository;

    @Transactional
    public SchoolResponseDTO execute(
            UserEntity currentUser,
            SchoolUpdateDTO dto,
            Long version
    ) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar escola. Escola não encontrada.");
                    return new SchoolNotFoundException();
                });

        if (!school.getVersion().equals(version)) {
            log.warn("Conflito de versao detectado em {}: {}", school.getId(), school.getVersion());
            throw new OptimisticLockingFailureException("Versão obsoleta");
        }

        if (dto.cnpj() != null && !dto.cnpj().equals(school.getCnpj())) {
            if (schoolRepository.existsByCnpj(dto.cnpj())) {
                throw new SchoolCnpjAlreadyExistsException();
            }
        }

        if (dto.user() != null && dto.user().email() != null) {
            String newEmail = dto.user().email();
            String currentEmail = school.getUser().getEmail();

            if (!newEmail.equals(currentEmail)) {
                if (userRepository.existsByEmail(newEmail)) {
                    log.warn("Falha ao atualizar: E-mail {} já em uso.", newEmail);
                    throw new SchoolEmailAlreadyExistsException();
                }
            }
        }

        schoolConverter.updateEntityFromDTO(dto, school);

        if (dto.address() != null) {
            addressConverter.updateEntityFromDTO(dto.address(), school.getAddress());
        }

        if (dto.user() != null) {
            userConverter.updateEntityFromDTO(dto.user(), school.getUser());

            if (dto.user().password() != null && !dto.user().password().isBlank()) {
                school.getUser().setPassword(passwordEncoder.encode(dto.user().password()));
            }
        }

        return schoolConverter.toResponseDTO(schoolRepository.save(school));
    }
}
