package com.example.projectapi.domain.inscribe.model;

import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.student.model.StudentEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_inscribe_student_offer", columnNames = {"student_id", "offer_id"}),
                @UniqueConstraint(name = "uk_report_student_year", columnNames = {"student_id", "school_year"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InscribeEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InscribeStatus status = InscribeStatus.PENDENTE;

    // Relacionamento

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private OfferEntity offer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;
}
