package com.example.projectapi.application.subject.usecase;

import com.example.projectapi.application.subject.converter.SubjectConverter;
import com.example.projectapi.application.subject.dto.SubjectRequestDTO;
import com.example.projectapi.application.subject.dto.SubjectResponseDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.subject.model.SubjectEntity;
import com.example.projectapi.domain.subject.repository.SubjectRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import com.example.projectapi.infra.exception.subject.SubjectAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSubjectUseCase {

    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectConverter subjectConverter;

    @Transactional
    public SubjectResponseDTO execute(UserEntity currentUser, SubjectRequestDTO dto) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar materia. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        boolean exists = subjectRepository.existsByNameIgnoreCaseAndSchool_Id(dto.name(), school.getId());
        if (exists) {
            log.warn("Falha ao criar materia. Materia duplicada para escola {}.", school.getId());
            throw new SubjectAlreadyExistsException();
        }

        SubjectEntity subject = subjectConverter.toEntity(dto);
        subject.setSchool(school);

        SubjectEntity savedSubject = subjectRepository.save(subject);
        log.info("Materia criada com sucesso. Escola {}, materia {}.", school.getId(), savedSubject.getId());
        return subjectConverter.toResponseDTO(savedSubject);
    }
}
