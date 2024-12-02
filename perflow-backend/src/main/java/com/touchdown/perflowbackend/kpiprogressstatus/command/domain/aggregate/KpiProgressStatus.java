package com.touchdown.perflowbackend.kpiprogressstatus.command.domain.aggregate;

import com.touchdown.perflowbackend.kpi.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kpi_progress_status", schema = "perflow")
public class KpiProgressStatus {
    @Id
    @Column(name = "kpi_pass_id", nullable = false)
    private Long id;

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

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}