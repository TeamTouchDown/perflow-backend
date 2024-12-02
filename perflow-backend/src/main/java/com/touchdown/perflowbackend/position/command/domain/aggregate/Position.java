package com.touchdown.perflowbackend.position.command.domain.aggregate;

import jakarta.persistence.*;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer level;
}
