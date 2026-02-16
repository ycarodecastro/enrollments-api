package com.example.projectapi.domain.school.model;

import com.example.projectapi.domain.address.model.AddressEntity;
import com.example.projectapi.domain.offer.model.OfferEntity;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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
        name = "schools",
        indexes = {
                @Index(name = "idx_school_name", columnList = "name")
        }
)
public class SchoolEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    // Relacionamento

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
            mappedBy = "school",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OfferEntity> offers;

    @OneToMany(mappedBy = "school")
    private List<StudentEntity> students;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private List<TranscriptEntity> transcripts;
}
