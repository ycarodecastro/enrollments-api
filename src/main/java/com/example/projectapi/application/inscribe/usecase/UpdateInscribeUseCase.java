package com.example.projectapi.application.inscribe.usecase;

import com.example.projectapi.application.inscribe.converter.InscribeConverter;
import com.example.projectapi.application.inscribe.dto.InscribeResponseDTO;
import com.example.projectapi.application.inscribe.dto.InscribeUpdateDTO;
import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import com.example.projectapi.domain.inscribe.model.InscribeStatus;
import com.example.projectapi.domain.inscribe.repository.InscribeRepository;
import com.example.projectapi.domain.offer.repository.OfferRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.student.event.StudentEnrolledEvent;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.inscribe.InscribeAlreadyTranscriptException;
import com.example.projectapi.infra.exception.inscribe.InscribeNotFoundException;
import com.example.projectapi.infra.exception.inscribe.InscribeStudentAlreadySchool;
import com.example.projectapi.infra.exception.offer.OfferNoAvailableSeatsException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateInscribeUseCase {

    private final InscribeRepository inscribeRepository;
    private final SchoolRepository schoolRepository;
    private final InscribeConverter inscribeConverter;
    private final OfferRepository offerRepository;
    private final TranscriptRepository transcriptRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public InscribeResponseDTO execute(
            UserEntity currentUser,
            InscribeUpdateDTO dto,
            Long id
    ) {
        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar inscricao. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        InscribeEntity inscribe = inscribeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar inscricao. Inscricao nao encontrada.");
                    return new InscribeNotFoundException();
                });

        if (!inscribe.getOffer().getSchool().getId().equals(school.getId())) {
            log.warn("Falha ao atualizar inscricao. Permissao negada.");
            throw new ForbiddenException();
        }

        InscribeStatus previousStatus = inscribe.getStatus();
        inscribeConverter.updateEntityFromDTO(dto, inscribe);

        if (previousStatus != InscribeStatus.ACEITO && inscribe.getStatus() == InscribeStatus.ACEITO) {
            if (inscribe.getStudent().getSchool() != null
                    && !inscribe.getStudent().getSchool().getId().equals(inscribe.getOffer().getSchool().getId())) {
                log.warn("Falha ao atualizar inscricao. Estudante ja pertence a outra escola.");
                throw new InscribeStudentAlreadySchool();
            }

            if (transcriptRepository.existsByStudent_IdAndSchoolYear(inscribe.getStudent().getId(), inscribe.getOffer().getSchoolYear())) {
                log.error("Tentativa de aceite duplicado para o aluno {} no ano {}",
                        inscribe.getStudent().getId(), inscribe.getOffer().getSchoolYear());
                throw new InscribeAlreadyTranscriptException(); // Exception customizada que retorna 400 ou 409
            }


            int updatedRows = offerRepository.decrementAvailableSeats(inscribe.getOffer().getId());

            if (updatedRows == 0) {
                log.warn("Falha ao atualizar inscrição. Oferta sem vagas disponíveis.");
                throw new OfferNoAvailableSeatsException();
            }

            inscribe.getStudent().setSchool(inscribe.getOffer().getSchool());
            eventPublisher.publishEvent(
                    new StudentEnrolledEvent(
                            inscribe.getStudent().getId(),
                            inscribe.getStudent().getSchool().getId(),
                            inscribe.getOffer().getSchoolYear()
                    )
            );

            log.info(
                    "Evento de criação de boletim iniciado. Estudante {}",
                    inscribe.getStudent().getName()
            );
        }

        InscribeEntity updatedInscribe = inscribeRepository.save(inscribe);

        log.info("Atualizacao de inscricao concluida com sucesso.");
        return inscribeConverter.toResponseDTO(updatedInscribe);
    }
}
