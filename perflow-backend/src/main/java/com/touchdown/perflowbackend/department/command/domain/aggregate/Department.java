package com.touchdown.perflowbackend.department.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "department", schema = "perflow")
public class Department {
    @Id
    @Column(name = "dept_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manage_dept_id", nullable = false)
    private Department manageDept;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "responsibility", nullable = false)
    private String responsibility;

    @Column(name = "pic", nullable = false, length = 30)
    private String pic;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private Instant updateDatetime;

}