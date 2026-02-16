package com.example.projectapi.domain.transcript.repository;

import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<TranscriptEntity, Long> {
    Page<TranscriptEntity> findAllByStudent_Id(Long studentId, Pageable pageable);

    Optional<TranscriptEntity> findByStudent_IdAndSchoolYear(Long studentId, Year targetYear);
}