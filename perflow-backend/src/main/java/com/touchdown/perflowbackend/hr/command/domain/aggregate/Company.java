package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "company", schema = "perflow")
public class Company {

    @Id
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "chairman", nullable = false, length = 30)
    private String chairman;

    @Column(name = "establish", nullable = false)
    private LocalDateTime establish;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "annual_count", nullable = false)
    private Long annualCount;

    @Column(name = "payment_datetime", nullable = false)
    private LocalDateTime paymentDatetime;

}