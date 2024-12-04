package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "annual", schema = "perflow")
public class Annual extends BaseEntity { //연차

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annual_id", nullable = false)
    private Long annualId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbjId;

    @Column(name = "enroll_annual", nullable = false)
    private LocalDateTime enrollAnnual;

    @Column(name = "annual_start", nullable = false)
    private LocalDateTime annualStart;

    @Column(name = "annual_end", nullable = false)
    private LocalDateTime annualEnd;

    @Column(name = "annual_status", nullable = false, length = 30)
    private Status annualStatus;

    @Column(name = "annual_reject_reason")
    private String annualRejectReason;

    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @ColumnDefault("0")
    @Column(name = "is_annual_retroactive", nullable = false)
    private Boolean isAnnualRetroactive = false; // 소급 여부 (0: 일반, 1: 소급)

    @Column(name = "annual_retroactive_reason", length = 255)
    private String annualRetroactiveReason; // 소급 사유

    @Column(name = "annual_retroactive_status", length = 30)
    private Status annualRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)



}