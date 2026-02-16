package com.example.projectapi.domain.transcript.model;


import com.example.projectapi.domain.grade.model.GradeEntity;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.student.model.StudentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.List;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "transcript",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_student_id_schoolYear", columnNames = {"student_id", "schoolYear"}),
        }
)
public class TranscriptEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private Year schoolYear; // Ex: "2026"

    // Relacionamentos

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL)
    private List<GradeEntity> grades; // Lista de todas as notas deste boletim
}
