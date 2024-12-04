package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "weight", schema = "perflow")
public class Weight {
    @Id
    @Column(name = "weight_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weightId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "personal_weight", nullable = false)
    private Long personalWeight;

    @Column(name = "team_weight", nullable = false)
    private Long teamWeight;

    @Column(name = "col_weight", nullable = false)
    private Long colWeight;

    @Column(name = "downward_weight", nullable = false)
    private Long downwardWeight;

    @Column(name = "attendance_weight", nullable = false)
    private Long attendanceWeight;

    @Column(name = "update_reason", nullable = false)
    private String updateReason;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}