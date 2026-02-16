package com.example.projectapi.domain.school.repository;

import com.example.projectapi.domain.school.model.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<SchoolEntity> findByUserId(Long id);

    // Relação ( escola 1 - n aluno )

    // "Encontre a escola através do atributo 'id' que está dentro da entidade 'student'"
    Optional<SchoolEntity> findByStudents_Id(Long studentId);
}
