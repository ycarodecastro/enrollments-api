package com.example.projectapi.domain.student.model;

import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import com.example.projectapi.domain.school.model.SchoolEntity;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_student_rg", columnNames = "rg"),
                @UniqueConstraint(name = "uk_student_cpf", columnNames = "cpf")
        }
)
public class StudentEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String name;


    @Column(nullable = false, length = 9)
    private String rg;

    @Column(nullable = false, length = 11)
    private String cpf;

    @ToString.Include
    @Column(nullable = false)
    private LocalDate dateBirth;

    // Relacionamentos

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<InscribeEntity> inscribes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolEntity school;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TranscriptEntity> transcripts;
}
