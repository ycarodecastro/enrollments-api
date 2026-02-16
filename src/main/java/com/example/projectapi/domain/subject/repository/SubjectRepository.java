package com.example.projectapi.domain.subject.repository;

import com.example.projectapi.domain.subject.model.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    Optional<SubjectEntity> findByIdAndSchool_Id(Long id, Long schoolId);
    boolean existsByNameIgnoreCaseAndSchool_Id(String name, Long schoolId);
    List<SubjectEntity> findAllBySchool_IdOrderByNameAsc(Long schoolId);

    List<SubjectEntity> findBySchoolId(Long schoolId);
}
