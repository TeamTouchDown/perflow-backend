package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "kpi_progress_status", schema = "perflow")
public class KpiProgressStatus extends BaseEntity {

    @Id
    @Column(name = "kpi_progress_pass_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kpiProgressPassId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kpi_id", nullable = false)
    private Kpi kpi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "progress_status", nullable = false)
    private Double progressStatus;

    @Column(name = "update_reason", nullable = false)
    private String updateReason;
}