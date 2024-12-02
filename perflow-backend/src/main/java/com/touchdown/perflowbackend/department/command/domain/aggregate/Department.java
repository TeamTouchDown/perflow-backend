package com.touchdown.perflowbackend.department.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Department {

    @Id
    private Long deptId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String responsibility;

    @Column(nullable = false)
    private String pic; // 부서 담당자

    @Column(nullable = false)
    private String contact;
}
