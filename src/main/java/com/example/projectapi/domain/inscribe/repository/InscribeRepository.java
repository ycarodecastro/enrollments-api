package com.example.projectapi.domain.inscribe.repository;

import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscribeRepository extends JpaRepository<InscribeEntity, Long> {

    Optional<InscribeEntity> findByIdAndOfferSchoolId(Long id, Long schoolId);

    @EntityGraph(attributePaths = {"student", "offer"})
    Page<InscribeEntity> findAllByOfferSchoolId(Long schoolId, Pageable pageable);

    Optional<InscribeEntity> findByStudentIdAndOfferId(Long studentId, Long offerId);

    boolean existsByStudentIdAndOfferId(Long id, Long id1);
}

