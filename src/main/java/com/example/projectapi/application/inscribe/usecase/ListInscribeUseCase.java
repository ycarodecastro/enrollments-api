package com.example.projectapi.application.inscribe.usecase;

import com.example.projectapi.application.inscribe.converter.InscribeConverter;
import com.example.projectapi.application.inscribe.dto.InscribeResponseDTO;
import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import com.example.projectapi.domain.inscribe.repository.InscribeRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListInscribeUseCase {

    private final InscribeRepository inscribeRepository;
    private final SchoolRepository schoolRepository;
    private final InscribeConverter inscribeConverter;

    public Page<InscribeResponseDTO> execute(
            UserEntity currentUser,
            Pageable pageable
    ) {

        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao listar inscrições. Escola não encontrada.");
                    return new SchoolNotFoundException();
                });

        Page<InscribeEntity> inscribes = inscribeRepository.findAllByOfferSchoolId(school.getId(), pageable);

        log.info("Listagem de inscrições concluída com sucesso.");
        return inscribes.map(inscribeConverter::toResponseDTO);

    }
}
