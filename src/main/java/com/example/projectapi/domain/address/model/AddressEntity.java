package com.example.projectapi.domain.address.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AddressEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false, length = 8)
    private String cep;

    @ToString.Include
    @Column(nullable = false)
    private String city;

    @ToString.Include
    @Column(nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private StateUF state;

    @ToString.Include
    @Column(nullable = false)
    private String street;

    @ToString.Include
    @Column(nullable = false)
    private String neighborhood;

    @ToString.Include
    @Column(nullable = false)
    private Integer number;

    @Column(nullable = true)
    private String complement;

}
