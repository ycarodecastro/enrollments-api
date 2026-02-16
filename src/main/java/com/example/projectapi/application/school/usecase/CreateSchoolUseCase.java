package com.example.projectapi.application.school.usecase;

import com.example.projectapi.application.school.converter.SchoolConverter;
import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.model.UserRole;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.exception.school.SchoolCnpjAlreadyExistsException;
import com.example.projectapi.infra.exception.school.SchoolEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    private final SchoolConverter schoolConverter;
    private final PasswordEncoder passwordEncoder;

    public SchoolResponseDTO execute(SchoolRequestDTO dto) {
        if (userRepository.existsByEmail(dto.user().email())) {
            log.warn("Falha ao criar escola. Email já cadastrado.");
            throw new SchoolEmailAlreadyExistsException();
        }

        if (schoolRepository.existsByCnpj(dto.cnpj())) {
            log.warn("Falha ao criar escola. CNPJ já cadastrado.");
            throw new SchoolCnpjAlreadyExistsException();
        }

        SchoolEntity school = schoolConverter.toEntity(dto);

        UserEntity user = UserEntity.builder()
                .email(dto.user().email())
                .password(passwordEncoder.encode(dto.user().password()))
                .role(UserRole.ESCOLA)
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


        school.setUser(user);
        school.setAddress(address);

        SchoolEntity savedSchool = schoolRepository.save(school);

        log.info("Criação de escola concluída com sucesso.");
        return schoolConverter.toResponseDTO(savedSchool);
    }
}
