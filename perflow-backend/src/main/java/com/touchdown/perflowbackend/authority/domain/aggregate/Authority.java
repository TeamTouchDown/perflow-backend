package com.touchdown.perflowbackend.authority.domain.aggregate;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "authority", schema = "perflow")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    @Column(name = "type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AuthType type;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AuthorityEmployee> authorityEmployees = new HashSet<>();
}