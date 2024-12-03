package com.touchdown.perflowbackend.annual.command.domain.aggregate;

import com.touchdown.perflowbackend.approvesbj.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "annual", schema = "perflow")
public class Annual {
    @Id
    @Column(name = "annual_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbj;

    @Column(name = "enroll_annual", nullable = false)
    private Instant enrollAnnual;

    @Column(name = "annual_start", nullable = false)
    private Instant annualStart;

    @Column(name = "annual_end", nullable = false)
    private Instant annualEnd;

    @Column(name = "annual_status", nullable = false, length = 30)
    private String annualStatus;

    @Column(name = "annual_reject_reason")
    private String annualRejectReason;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @ColumnDefault("0")
    @Column(name = "is_annual_retroactive", nullable = false)
    private Boolean isAnnualRetroactive = false; // 소급 여부 (0: 일반, 1: 소급)

    @Column(name = "annual_retroactive_reason", length = 255)
    private String annualRetroactiveReason; // 소급 사유

    @Column(name = "annual_retrospective_status", length = 30)
    private String annualRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)



}