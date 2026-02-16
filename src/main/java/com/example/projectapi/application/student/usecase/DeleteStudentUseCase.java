package com.example.projectapi.application.student.usecase;

import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteStudentUseCase {

    private final StudentRepository studentRepository;

    @Transactional
    public void execute(UserEntity currentUser) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Falha ao excluir estudante. Estudante não encontrado.");
                    return new StudentNotFoundException();
                });

        log.info("Exclusão de estudante concluída com sucesso.");
        studentRepository.delete(student);
    }
}
