package com.example.projectapi.application.school.usecase;

import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.school.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void execute(UserEntity currentUser) {

        SchoolEntity school = schoolRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao excluir escola. Escola nao encontrada.");
                    return new SchoolNotFoundException();
                });

        // Se uma escola for excluida todos os alunos atribuidos a elas possuiram o
        // school.id vai ser nulo
        List<StudentEntity> students = studentRepository.findAllBySchoolId(school.getId());
        students.forEach(student -> student.setSchool(null));
        studentRepository.saveAll(students);

        schoolRepository.delete(school);
        log.info("Exclusao de escola concluida com sucesso.");
    }
}
