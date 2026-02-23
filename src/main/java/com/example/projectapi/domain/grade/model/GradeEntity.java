package com.example.projectapi.domain.grade.model;

import com.example.projectapi.domain.subject.model.SubjectEntity;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "grade")
public class GradeEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    @DecimalMax("10.0")
    @DecimalMin("0.0")
    private Double value; // A nota em si (ex: 8.5)

    @ToString.Include
    @Column(nullable = false)
    private String period; // Ex: "1º Bimestre", "Exame Final"

    // Adicionando um sistema de concorrência
    @Version
    private Long version;

    // Relacionamento
    @ManyToOne
    @JoinColumn(name = "transcript_id", nullable = false)
    private TranscriptEntity transcript;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;
}
