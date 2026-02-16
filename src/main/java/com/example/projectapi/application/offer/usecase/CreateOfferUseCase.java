package com.example.projectapi.application.offer.usecase;

import com.example.projectapi.application.offer.converter.OfferConverter;
import com.example.projectapi.application.offer.dto.OfferRequestDTO;
import com.example.projectapi.application.offer.dto.OfferResponseDTO;
import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.offer.repository.OfferRepository;
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
public class CreateOfferUseCase {

    private final OfferRepository offerRepository;
    private final OfferConverter offerConverter;

    private final SchoolRepository schoolRepository;

    public OfferResponseDTO execute(
            UserEntity currentUser,
            OfferRequestDTO dto
    ) {

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar oferta. Escola não encontrada.");
                    return new SchoolNotFoundException();
                });


        OfferEntity offer = offerConverter.toEntity(dto);

        offer.setSchool(school);

        OfferEntity savedOffer = offerRepository.save(offer);

        log.info("Criação de oferta concluída com sucesso.");
        return offerConverter.toResponseDTO(savedOffer);


    }
}
