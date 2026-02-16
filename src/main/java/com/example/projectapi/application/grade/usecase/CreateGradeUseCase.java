package com.example.projectapi.application.grade.usecase;

import com.example.projectapi.application.grade.converter.GradeConverter;
import com.example.projectapi.application.grade.dto.GradeRequestDTO;
import com.example.projectapi.application.grade.dto.GradeResponseDTO;
import com.example.projectapi.domain.grade.model.GradeEntity;
import com.example.projectapi.domain.grade.repository.GradeRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.subject.model.SubjectEntity;
import com.example.projectapi.domain.subject.repository.SubjectRepository;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.grade.GradeAlreadyExistsException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import com.example.projectapi.infra.exception.subject.SubjectNotFoundException;
import com.example.projectapi.infra.exception.transcript.TranscriptNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateGradeUseCase {

    private final SchoolRepository schoolRepository;
    private final TranscriptRepository transcriptRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final GradeConverter gradeConverter;

    @Transactional
    public GradeResponseDTO execute(
            UserEntity currentUser,
            Long transcriptId,
            GradeRequestDTO dto
    ) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar nota. Escola nao encontrada.");
                    return new SchoolNotFoundException();
        });

        TranscriptEntity transcript = transcriptRepository.findById(transcriptId)
                .orElseThrow(() -> {
                    log.warn("Falha ao criar nota. Boletim nao encontrado.");
                    return new TranscriptNotFoundException();
        });

        if (!transcript.getSchool().getId().equals(school.getId())) {
            log.warn("Falha ao criar nota. Escola sem permissao no boletim {}.", transcriptId);
            throw new ForbiddenException();
        }

        SubjectEntity subject = subjectRepository.findByIdAndSchool_Id(dto.subjectId(), school.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar nota. Materia {} nao encontrada para a escola {}.", dto.subjectId(), school.getId());
                    return new SubjectNotFoundException();
        });

        boolean duplicated = gradeRepository.existsByTranscript_IdAndSubject_IdAndPeriodIgnoreCase(
                transcriptId,
                subject.getId(),
                dto.period()
        );
        if (duplicated) {
            log.warn(
                    "Falha ao criar nota. Nota duplicada para boletim {}, materia {} e periodo {}.",
                    transcriptId,
                    subject.getId(),
                    dto.period()
            );
            throw new GradeAlreadyExistsException();
        }

        GradeEntity grade = gradeConverter.toEntity(dto);
        grade.setTranscript(transcript);
        grade.setSubject(subject);

        GradeEntity savedGrade = gradeRepository.save(grade);
        log.info("Nota criada com sucesso. Boletim {}, nota {}.", transcriptId, savedGrade.getId());
        return gradeConverter.toResponseDTO(savedGrade);
    }
}
