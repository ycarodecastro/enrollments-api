package com.example.projectapi.domain.offer.repository;

import com.example.projectapi.domain.offer.model.OfferEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
    List<OfferEntity> findBySchoolId(Long idSchool);

    Optional<OfferEntity> findById(Long offerId);

    @Transactional
    void deleteByEndDateLessThanEqual(LocalDate date);

    // ver se tem uma forma mais profissional de diminuir a quantidade de ofertas
    @Modifying
    @Query("UPDATE OfferEntity o SET o.availableSeats = o.availableSeats - 1 " +
            "WHERE o.id = :id AND o.availableSeats > 0")
    int decrementAvailableSeats(Long id);
}
