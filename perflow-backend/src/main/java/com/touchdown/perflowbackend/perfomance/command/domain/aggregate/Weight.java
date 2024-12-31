package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "weight", schema = "perflow")
public class Weight extends BaseEntity {

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

    @Builder
    public Weight(Department dept, Employee emp, Long personalWeight, Long teamWeight, Long colWeight, Long downwardWeight, Long attendanceWeight, String reason) {
        this.dept = dept;
        this.emp = emp;
        this.personalWeight = personalWeight;
        this.teamWeight = teamWeight;
        this.colWeight = colWeight;
        this.downwardWeight = downwardWeight;
        this.attendanceWeight = attendanceWeight;
        this.updateReason = reason;
    }
}