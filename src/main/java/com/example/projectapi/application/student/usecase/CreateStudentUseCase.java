package com.example.projectapi.application.student.usecase;

import com.example.projectapi.application.student.converter.StudentConverter;
import com.example.projectapi.application.student.dto.StudentRequestDTO;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.model.UserRole;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.exception.student.StudentCpfAlreadyExistsException;
import com.example.projectapi.infra.exception.student.StudentEmailAlreadyExistsException;
import com.example.projectapi.infra.exception.student.StudentRgAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateStudentUseCase {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final StudentConverter studentConverter;

    public StudentResponseDTO execute(StudentRequestDTO dto) {

        if (userRepository.existsByEmail(dto.user().email())) {
            log.warn("Falha ao criar estudante. Email já cadastrado.");
            throw new StudentEmailAlreadyExistsException();
        }

        if (studentRepository.existsByCpf(dto.cpf())) {
            log.warn("Falha ao criar estudante. CPF já cadastrado.");
            throw new StudentCpfAlreadyExistsException();
        }

        if (studentRepository.existsByRg(dto.rg())) {
            log.warn("Falha ao criar estudante. RG já cadastrado.");
            throw new StudentRgAlreadyExistsException();
        }

        StudentEntity student = studentConverter.toEntity(dto);

        UserEntity user = UserEntity.builder()
                .email(dto.user().email())
                .password(passwordEncoder.encode(dto.user().password()))
                .role(UserRole.ALUNO)
                .active(true)
                .build();

        AddressEntity address = AddressEntity.builder()
                .cep(dto.address().cep())
                .city(dto.address().city())
                .state(dto.address().state())
                .street(dto.address().street())
                .neighborhood(dto.address().neighborhood())
                .number(dto.address().number())
                .complement(dto.address().complement())
                .build();


        student.setUser(user);
        student.setAddress(address);

        StudentEntity savedStudent = studentRepository.save(student);

        log.info("Criação de estudante concluída com sucesso.");
        return studentConverter.toResponseDTO(savedStudent);
    }
}
