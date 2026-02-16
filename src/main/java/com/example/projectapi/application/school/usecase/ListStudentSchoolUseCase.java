package com.example.projectapi.application.school.usecase;

import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListStudentSchoolUseCase {
    private final SchoolRepository schoolRepository;
    private final StudentConverter studentConverter;
    private final StudentRepository studentRepository;

    public Page<StudentResponseDTO> execute(
            UserEntity currentUser,
            Pageable pageable
            ) {

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao mostrar lista de alunos. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        Page<StudentEntity> students = studentRepository.findAllBySchoolId(school.getId(), pageable);
        log.info("Listagem de alunos vinculada a escola concluída com sucesso.");

        return students.map(studentConverter :: toResponseDTO);
    }
}
