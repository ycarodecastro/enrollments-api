package com.example.projectapi.application.transcript.usecase;

import com.example.projectapi.application.transcript.converter.TranscriptConverter;
import com.example.projectapi.application.transcript.dto.TranscriptResponseDTO;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAllTranscriptUseCase {

    private final StudentRepository studentRepository;

    private final TranscriptRepository transcriptRepository;
    private final TranscriptConverter transcriptConverter;

    public Page<TranscriptResponseDTO> execute (
            UserEntity currentUser,
            Pageable pageable
    ){
        Long userId = currentUser != null ? currentUser.getId() : null;
        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Falha ao criar boletim. Estudante não encontrado.");
                    return new StudentNotFoundException();
                });

        Page<TranscriptEntity> transcripts = transcriptRepository.findAllByStudent_Id(student.getId(), pageable);

        log.info(
                "Lista de todos os boletins do estudante criados. Estudante {}",
                student.getName()
        );

        return transcripts.map(transcriptConverter::toResponseDTO);
    }
}
