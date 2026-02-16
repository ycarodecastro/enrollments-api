package com.example.projectapi.domain.refreshToken.model;

import com.example.projectapi.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.EqualityComparator;

import java.time.Instant;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "refreshToken")
public class RefreshTokenEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne // Vários tokens podem pertencer a um utilizador
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(nullable = false)
    private Instant expiryDate;

}
