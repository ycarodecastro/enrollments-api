package com.example.projectapi.application.student.usecase;

import com.example.projectapi.application.address.converter.AddressConverter;
import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.application.student.dto.StudentUpdateDTO;
import com.example.projectapi.application.user.converter.UserConverter;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateStudentUseCase {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final AddressConverter addressConverter;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponseDTO execute(Long id, StudentUpdateDTO dto) {

        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar estudante. Estudante não encontrado. Id: {}", id);
                    return new StudentNotFoundException();
                });

        return updateStudent(student, dto);
    }

    @Transactional
    public StudentResponseDTO execute(UserEntity currentUser, StudentUpdateDTO dto) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        StudentEntity student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar estudante. Estudante não encontrado. UserId: {}", userId);
                    return new StudentNotFoundException();
                });

        return updateStudent(student, dto);
    }

    private StudentResponseDTO updateStudent(StudentEntity student, StudentUpdateDTO dto) {
        studentConverter.updateEntityFromDTO(dto, student);

        if (dto.address() != null) {
            addressConverter.updateEntityFromDTO(dto.address(), student.getAddress());
        }
        if (dto.user() != null) {

            if (dto.user().password() != null && !dto.user().password().isBlank()) {
                student.getUser().setPassword(passwordEncoder.encode(dto.user().password()));
            }

            userConverter.updateEntityFromDTO(dto.user(), student.getUser());
        }

        StudentEntity updatedStudent = studentRepository.save(student);

        log.info("Atualização de estudante concluída com sucesso.");
        return studentConverter.toResponseDTO(updatedStudent);
    }
}
