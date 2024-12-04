package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "kpi_limit", schema = "perflow")
public class KpiLimit extends BaseEntity {

    @Id
    @Column(name = "kpi_limit_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kpiLimitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "personal_kpi_min", nullable = false)
    private Long personalKpiMin;

    @Column(name = "personal_kpi_max", nullable = false)
    private Long personalKpiMax;

    @Column(name = "team_kpi_min", nullable = false)
    private Long teamKpiMin;

    @Column(name = "team_kpi_max", nullable = false)
    private Long teamKpiMax;
}
