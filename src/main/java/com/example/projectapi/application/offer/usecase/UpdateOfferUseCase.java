package com.example.projectapi.application.offer.usecase;

import com.example.projectapi.application.offer.converter.OfferConverter;
import com.example.projectapi.application.offer.dto.OfferResponseDTO;
import com.example.projectapi.application.offer.dto.OfferUpdateDTO;
import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.offer.repository.OfferRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.offer.OfferNotFoundException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOfferUseCase {

    private final OfferRepository offerRepository;
    private final OfferConverter offerConverter;
    private final SchoolRepository schoolRepository;

    @Transactional
    public OfferResponseDTO execute(
            UserEntity currentUser,
            OfferUpdateDTO dto,
            Long id
    ) {

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar oferta. Escola não encontrada.");
                    return new SchoolNotFoundException();
                });

        OfferEntity offer = offerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar oferta. Oferta não encontrada.");
                    return new OfferNotFoundException();
                });

        if (!offer.getSchool().getId().equals(school.getId())) {
            log.warn("Falha ao atualizar oferta. Permissão negada.");
            throw new ForbiddenException();
        }

        offerConverter.updateEntityFromDTO(dto, offer);

        offer.setSchool(school);

        OfferEntity updatedOffer = offerRepository.save(offer);

        log.info("Atualização de oferta concluída com sucesso.");
        return offerConverter.toResponseDTO(updatedOffer);
    }
}
