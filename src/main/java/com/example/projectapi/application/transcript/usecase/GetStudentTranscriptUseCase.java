package com.example.projectapi.application.transcript.usecase;

import com.example.projectapi.application.transcript.converter.TranscriptConverter;
import com.example.projectapi.application.transcript.dto.TranscriptResponseDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import com.example.projectapi.infra.exception.transcript.TranscriptNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetStudentTranscriptUseCase {

    private final SchoolRepository schoolRepository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptConverter transcriptConverter;

    @Transactional(readOnly = true)
    public TranscriptResponseDTO execute(
            UserEntity currentUser,
            Long idStudent,
            Year schoolYear
    ) {
        if (currentUser == null || currentUser.getId() == null) {
            log.error("Tentativa de acesso ao boletim de aluno negada. Sem usuario autenticado.");
            throw new SchoolNotFoundException();
        }

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn(
                            "Falha ao buscar boletim de aluno. Escola nao encontrada para o user {}.",
                            currentUser.getId()
                    );
                    return new SchoolNotFoundException();
                });

        TranscriptEntity transcript = transcriptRepository.findByStudent_IdAndSchoolYear(idStudent, schoolYear)
                .orElseThrow(() -> {
                    log.warn(
                            "Falha ao buscar boletim de aluno. Boletim nao encontrado para aluno {} no ano {}.",
                            idStudent,
                            schoolYear
                    );
                    return new TranscriptNotFoundException();
                });

        if (!transcript.getSchool().getId().equals(school.getId())) {
            log.warn(
                    "Escola {} tentou acessar boletim de aluno que nao lhe pertence.",
                    school.getId()
            );
            throw new ForbiddenException();
        }

        log.info(
                "Boletim de aluno {} no ano {} recuperado com sucesso para escola {}.",
                idStudent,
                schoolYear,
                school.getId()
        );
        return transcriptConverter.toResponseDTO(transcript);
    }
}
