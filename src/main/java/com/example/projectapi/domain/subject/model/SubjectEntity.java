package com.example.projectapi.domain.subject.model;

import com.example.projectapi.domain.school.model.SchoolEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "subject",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"name", "school_id"}) // Permite que a escola não adicione nomes repetitivos
        }
)
public class SubjectEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school; // A matéria obrigatoriamente pertence a uma escola
}
