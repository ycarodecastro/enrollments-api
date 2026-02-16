package com.example.projectapi.application.grade.usecase;

import com.example.projectapi.domain.grade.model.GradeEntity;
import com.example.projectapi.domain.grade.repository.GradeRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.ForbiddenException;
import com.example.projectapi.infra.exception.grade.GradeNotFoundException;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteGradeUseCase {

    private final SchoolRepository schoolRepository;
    private final GradeRepository gradeRepository;

    @Transactional
    public void execute(
            UserEntity currentUser,
            Long transcriptId,
            Long gradeId
    ) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao remover nota. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        GradeEntity grade = gradeRepository.findByIdAndTranscript_Id(gradeId, transcriptId)
                .orElseThrow(() -> {
                    log.warn("Falha ao remover nota. Nota nao encontrada.");
                    return new GradeNotFoundException();
                });

        if (!grade.getTranscript().getSchool().getId().equals(school.getId())) {
            log.warn("Falha ao remover nota. Escola sem permissao na nota {}.", gradeId);
            throw new ForbiddenException();
        }

        gradeRepository.delete(grade);
        log.info("Nota removida com sucesso. Boletim {}, nota {}.", transcriptId, gradeId);
    }
}
