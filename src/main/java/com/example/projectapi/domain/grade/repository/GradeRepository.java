package com.example.projectapi.domain.grade.repository;

import com.example.projectapi.domain.grade.model.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {

    boolean existsByTranscript_IdAndSubject_IdAndPeriodIgnoreCase(Long transcriptId, Long subjectId, String period);

    boolean existsByTranscript_IdAndSubject_IdAndPeriodIgnoreCaseAndIdNot(
            Long transcriptId,
            Long subjectId,
            String period,
            Long id
    );

    Optional<GradeEntity> findByIdAndTranscript_Id(Long id, Long transcriptId);
}
