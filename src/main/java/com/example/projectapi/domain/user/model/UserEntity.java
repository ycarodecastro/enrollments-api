package com.example.projectapi.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter // Gera todos os Getters
@Setter // Gera todos os Setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
        }
)
public class UserEntity {

    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ToString.Include
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @ToString.Include
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
