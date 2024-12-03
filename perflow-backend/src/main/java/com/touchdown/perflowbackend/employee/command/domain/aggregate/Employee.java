package com.touchdown.perflowbackend.employee.command.domain.aggregate;

import com.touchdown.perflowbackend.department.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.job.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.position.command.domain.aggregate.Position;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "employee", schema = "perflow")
public class Employee {
    @Id
    @Column(name = "emp_id", nullable = false, length = 30)
    private String empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "gender", nullable = false, length = 30)
    private String gender;

    @Column(name = "rrn", nullable = false, length = 30)
    private String rrn;

    @Column(name = "pay", nullable = false)
    private Long pay;

    @Column(name = "address", nullable = false, length = 30)
    private String address;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}