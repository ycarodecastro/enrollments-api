package com.example.projectapi.integration;

import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.address.model.StateUF;
import com.example.projectapi.domain.address.repository.AddressRepository;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.school.repository.SchoolRepository;
import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.model.UserRole;
import com.example.projectapi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class ListSchoolUseCaseTest {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Should create and list schools in database")
    void shouldCreateAndListSchool() {

        // Cria usuário (sem setar ID)
        UserEntity user = UserEntity.builder()
                .email("teste@escola.com")
                .password("123456")
                .active(true)
                .role(UserRole.ESCOLA)
                .build();

        userRepository.saveAndFlush(user);

        // Cria endereço (sem setar ID)
        AddressEntity address = AddressEntity.builder()
                .cep("12345678")
                .street("Rua A")
                .neighborhood("Centro")
                .city("São Paulo")
                .state(StateUF.SP)
                .number(100)
                .complement("")
                .build();

        addressRepository.saveAndFlush(address);

        // Cria escola
        SchoolEntity school = SchoolEntity.builder()
                .name("Patronato")
                .cnpj("12345678900011")
                .users(user)
                .address(address)
                .build();

        // Salva no banco (cascade salva user e address)
        schoolRepository.saveAndFlush(school);

        // Busca tudo
        List<SchoolEntity> schools = schoolRepository.findAll();

        // Confirma
        assertFalse(schools.isEmpty());
        schools.forEach(s -> System.out.println("Escola: " + s.getName()
                + ", ID: " + s.getId()
                + ", Usuário ID: " + s.getUsers().getId()
                + ", Endereço ID: " + s.getAddress().getId()));
    }
}

