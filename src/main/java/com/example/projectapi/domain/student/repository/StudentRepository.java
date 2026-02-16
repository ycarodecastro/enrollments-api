package com.example.projectapi.domain.student.repository;

import com.example.projectapi.domain.student.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    // validações no Create
    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);

    Optional<StudentEntity> findByUserId(Long id);

    Page<StudentEntity> findAllBySchoolId(Long schoolId, Pageable pageable);

    List<StudentEntity> findAllBySchoolId(Long schoolId);
}
