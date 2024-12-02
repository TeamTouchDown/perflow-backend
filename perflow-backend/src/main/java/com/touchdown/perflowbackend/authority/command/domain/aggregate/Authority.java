package com.touchdown.perflowbackend.authority.command.domain.aggregate;

import jakarta.persistence.*;

@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    @Column(nullable = false)
    private String type;

}
