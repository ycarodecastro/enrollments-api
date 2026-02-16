package com.example.projectapi.domain.offer.model;

import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import com.example.projectapi.domain.school.model.SchoolEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "offers",
        indexes = {
                @Index(name = "idx_offer_grade_class", columnList = "grade, classGroup")
        }
)
public class OfferEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String grade;

    @ToString.Include
    @Column(nullable = false)
    private String classGroup;

    @ToString.Include
    @Column(nullable = false)
    private String shift;

    @ToString.Include
    @Column(nullable = false)
    private Year schoolYear;

    @ToString.Include
    @Column(nullable = false)
    private Integer availableSeats;

    @ToString.Include
    @Column(nullable = false)
    private LocalDate startDate;

    @ToString.Include
    @Column(nullable = false)
    private LocalDate endDate;

    @PrePersist
    protected void onCreate() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;

    @OneToMany(
            mappedBy = "offer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<InscribeEntity> inscribes = new HashSet<>();
}
