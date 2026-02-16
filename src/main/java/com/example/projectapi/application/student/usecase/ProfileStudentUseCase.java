package com.example.projectapi.application.student.usecase;

import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileStudentUseCase {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    public StudentResponseDTO execute(UserEntity currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            log.error(
                    "Tentativa de acesso ao perfil negada. Sem usuario autenticado."
            );
            throw new StudentNotFoundException();
        }

        StudentEntity student = studentRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn(
                            "Estudante não encontrado para o User ID: {}",
                            currentUser.getId()
                    );
                    return new StudentNotFoundException();
                });

        log.info(
                "Perfil do estudante {} recuperado com sucesso.",
                student.getName()
        );
        return studentConverter.toResponseDTO(student);
    }
}
