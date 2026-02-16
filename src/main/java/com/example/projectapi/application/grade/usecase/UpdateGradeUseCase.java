package com.example.projectapi.application.grade.usecase;

import com.example.projectapi.application.grade.converter.GradeConverter;
import com.example.projectapi.application.grade.dto.GradeResponseDTO;
import com.example.projectapi.application.grade.dto.GradeUpdateDTO;
import com.example.projectapi.domain.grade.model.GradeEntity;
import com.example.projectapi.domain.grade.repository.GradeRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.grade.GradeAlreadyExistsException;
import com.example.projectapi.infra.exception.grade.GradeNotFoundException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateGradeUseCase {

    private final SchoolRepository schoolRepository;
    private final GradeRepository gradeRepository;
    private final GradeConverter gradeConverter;

    @Transactional
    public GradeResponseDTO execute(
            UserEntity currentUser,
            Long transcriptId,
            Long gradeId,
            GradeUpdateDTO dto
    ) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar nota. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        GradeEntity grade = gradeRepository.findByIdAndTranscript_Id(gradeId, transcriptId)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar nota. Nota nao encontrada.");
                    return new GradeNotFoundException();
                });

        if (!grade.getTranscript().getSchool().getId().equals(school.getId())) {
            log.warn("Falha ao atualizar nota. Escola sem permissao na nota {}.", gradeId);
            throw new ForbiddenException();
        }

        if (dto.period() != null && !dto.period().isBlank()) {
            boolean duplicated = gradeRepository.existsByTranscript_IdAndSubject_IdAndPeriodIgnoreCaseAndIdNot(
                    transcriptId,
                    grade.getSubject().getId(),
                    dto.period(),
                    gradeId
            );
            if (duplicated) {
                log.warn(
                        "Falha ao atualizar nota. Duplicidade para boletim {}, materia {} e periodo {}.",
                        transcriptId,
                        grade.getSubject().getId(),
                        dto.period()
                );
                throw new GradeAlreadyExistsException();
            }
        }

        gradeConverter.updateEntityFromDTO(dto, grade);
        GradeEntity updatedGrade = gradeRepository.save(grade);
        log.info("Nota atualizada com sucesso. Boletim {}, nota {}.", transcriptId, gradeId);
        return gradeConverter.toResponseDTO(updatedGrade);
    }
}
