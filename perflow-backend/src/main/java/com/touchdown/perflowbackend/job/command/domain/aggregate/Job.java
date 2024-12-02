package com.touchdown.perflowbackend.job.command.domain.aggregate;

import jakarta.persistence.*;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String responsibility;

    @Column
    private boolean isActive;
}
