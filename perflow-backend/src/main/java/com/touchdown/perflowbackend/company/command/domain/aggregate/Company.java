package com.touchdown.perflowbackend.company.command.domain.aggregate;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String chairman;

    @Column(nullable = false)
    private LocalDate establish;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer annualCount;

    @Column(nullable = false)
    private LocalDateTime paymentDatetime;
}
