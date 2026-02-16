package com.example.projectapi.application.inscribe.usecase;

import com.example.projectapi.application.inscribe.converter.InscribeConverter;
import com.example.projectapi.application.inscribe.dto.InscribeRequestDTO;
import com.example.projectapi.application.inscribe.dto.InscribeResponseDTO;
import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import com.example.projectapi.domain.inscribe.repository.InscribeRepository;
import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.offer.repository.OfferRepository;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.inscribe.InscribeAlreadyExistsException;
import com.example.projectapi.infra.exception.offer.OfferNotFoundException;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CreateInscribeUseCase {

    private final StudentRepository studentRepository;
    private final OfferRepository offerRepository;
    private final InscribeRepository inscribeRepository;
    private final InscribeConverter inscribeConverter;

    @Transactional
    public InscribeResponseDTO execute(UserEntity currentUser, InscribeRequestDTO dto) {

        StudentEntity student = studentRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao encontrar estudante. Estudante não encontrado.");
                    return new StudentNotFoundException();
                });

        OfferEntity offer = offerRepository.findById(dto.offer_id())
                .orElseThrow(() -> {
                    log.warn("Falha ao encontrar oferta. Oferta não encontrada.");
                    return new OfferNotFoundException();
                });

        boolean alreadyInscribed = inscribeRepository.existsByStudentIdAndOfferId(student.getId(), offer.getId());

        if (alreadyInscribed) {
            log.warn("Falha ao criar inscrição. Estudante já inscrito na oferta.");
            throw new InscribeAlreadyExistsException();
        }

        InscribeEntity inscribe = inscribeConverter.toEntity(dto);
        inscribe.setStudent(student);
        inscribe.setOffer(offer);

        log.info("Criação de inscrição concluída com sucesso.");
        return inscribeConverter.toResponseDTO(inscribeRepository.save(inscribe));
    }
}
