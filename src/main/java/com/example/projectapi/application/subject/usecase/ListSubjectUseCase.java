package com.example.projectapi.application.subject.usecase;

import com.example.projectapi.application.subject.converter.SubjectConverter;
import com.example.projectapi.application.subject.dto.SubjectResponseDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.subject.repository.SubjectRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListSubjectUseCase {

    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectConverter subjectConverter;

    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> execute(UserEntity currentUser) {
        SchoolEntity school = schoolRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao listar materias. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        return subjectRepository.findAllBySchool_IdOrderByNameAsc(school.getId())
                .stream()
                .map(subjectConverter::toResponseDTO)
                .toList();
    }
}
