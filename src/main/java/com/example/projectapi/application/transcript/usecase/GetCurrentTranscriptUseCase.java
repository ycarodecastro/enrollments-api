package com.example.projectapi.application.transcript.usecase;

import com.example.projectapi.application.transcript.converter.TranscriptConverter;
import com.example.projectapi.application.transcript.dto.TranscriptResponseDTO;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import com.example.projectapi.infra.exception.transcript.TranscriptNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCurrentTranscriptUseCase {

    private final StudentRepository studentRepository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptConverter transcriptConverter;

    @Transactional(readOnly = true)
    public TranscriptResponseDTO execute(
            UserEntity currentUser,
            Year schoolYear
    ) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Falha ao buscar boletim atual. Estudante nao encontrado.");
                    return new StudentNotFoundException();
        });

        Year targetYear = (schoolYear == null)
                ? Year.now()
                : schoolYear;

        TranscriptEntity transcript = transcriptRepository.findByStudent_IdAndSchoolYear(student.getId(), targetYear)
                .orElseThrow(() -> {
                    log.warn(
                            "Falha ao buscar boletim atual. Boletim nao encontrado para estudante {} e ano {}.",
                            student.getId(),
                            targetYear
                    );
                    return new TranscriptNotFoundException();
                });

        log.info("Boletim atual encontrado para estudante {} no ano {}.", student.getName(), targetYear);
        return transcriptConverter.toResponseDTO(transcript);
    }
}
