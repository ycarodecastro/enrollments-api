package com.example.projectapi.application.offer.usecase;

import com.example.projectapi.application.offer.converter.OfferConverter;
import com.example.projectapi.application.offer.dto.OfferResponseDTO;
import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListOfferUseCase {

    private final OfferRepository offerRepository;
    private final OfferConverter offerConverter;

    public Page<OfferResponseDTO> execute(Pageable pageable) {

        Page<OfferEntity> offer = offerRepository.findAll(pageable);

        log.info("Listagem de ofertas concluída com sucesso.");
        return offer.map(offerConverter::toResponseDTO);
    }
}
