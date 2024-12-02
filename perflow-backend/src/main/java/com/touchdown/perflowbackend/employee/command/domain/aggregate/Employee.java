package com.touchdown.perflowbackend.employee.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Employee {

    @Id
    @Column(unique = true, nullable = false)
    private String empId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime joinDate;

    @Column(nullable = false)
    private String status;


}
