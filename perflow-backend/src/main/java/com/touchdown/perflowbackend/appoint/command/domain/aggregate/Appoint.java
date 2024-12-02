package com.touchdown.perflowbackend.appoint.command.domain.aggregate;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Appoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String before;

    @Column(nullable = false)
    private String after;

    @Column(nullable = false)
    private LocalDateTime appointDatetime;
}
