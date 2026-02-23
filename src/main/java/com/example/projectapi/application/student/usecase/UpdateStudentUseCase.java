package com.example.projectapi.application.student.usecase;

import com.example.projectapi.application.address.converter.AddressConverter;
import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.application.student.dto.StudentUpdateDTO;
import com.example.projectapi.application.user.converter.UserConverter;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.exception.student.StudentEmailAlreadyExistsException;
import com.example.projectapi.infra.exception.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateStudentUseCase {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final AddressConverter addressConverter;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public StudentResponseDTO execute(
            UserEntity currentUser,
            StudentUpdateDTO dto,
            Long version
    ) {
        StudentEntity student = studentRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar estudante. Estudante não encontrado.");
                    return new StudentNotFoundException();
                });

        if (!student.getVersion().equals(version)) {
            log.warn("Conflito de versao detectado em {}: {}", student.getId(), student.getVersion());
            throw new OptimisticLockingFailureException("Versão obsoleta");
        }

        if (dto.user() != null && dto.user().email() != null) {
            String newEmail = dto.user().email();
            String currentEmail = student.getUser() != null ? student.getUser().getEmail() : null;

            if (!newEmail.equals(currentEmail)) {
                if (userRepository.existsByEmail(newEmail)) {
                    log.warn("Falha ao atualizar estudante. Email {} já cadastrado.", newEmail);
                    throw new StudentEmailAlreadyExistsException();
                }
            }
        }

        studentConverter.updateEntityFromDTO(dto, student);

        if (dto.address() != null) {
            addressConverter.updateEntityFromDTO(dto.address(), student.getAddress());
        }

        if (dto.user() != null) {
            userConverter.updateEntityFromDTO(dto.user(), student.getUser());

            // Password Hashing por último para garantir o override do converter
            if (dto.user().password() != null && !dto.user().password().isBlank()) {
                String hashed = passwordEncoder.encode(dto.user().password());
                student.getUser().setPassword(hashed);
            }
        }

        StudentEntity updatedStudent = studentRepository.save(student);

        log.info("Atualização de estudante concluída com sucesso.");
        return studentConverter.toResponseDTO(updatedStudent);
    }
}
