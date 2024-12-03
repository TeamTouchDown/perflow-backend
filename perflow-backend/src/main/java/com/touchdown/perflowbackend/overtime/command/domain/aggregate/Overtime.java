package com.touchdown.perflowbackend.overtime.command.domain.aggregate;

import com.touchdown.perflowbackend.approvesbj.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "overtime", schema = "perflow")
public class Overtime {
    @Id
    @Column(name = "overtime_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbj;

    @Column(name = "overtime_type", nullable = false, length = 30)
    private String overtimeType;

    @Column(name = "enroll_overtime", nullable = false)
    private Instant enrollOvertime;

    @Column(name = "overtime_start", nullable = false)
    private Instant overtimeStart;

    @Column(name = "overtime_end", nullable = false)
    private Instant overtimeEnd;

    @Column(name = "overtime_status", nullable = false, length = 30)
    private String overtimeStatus;

    @Column(name = "travel_reject_time")
    private String travelRejectTime;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "is_overtime_retroactive", nullable = false)
    private Boolean isOvertimeRetroactive = false; // 소급 여부 (0: 일반, 1: 소급)

    @Column(name = "overtime_retroactive_reason", length = 255)
    private String overtimeRetroactiveReason; // 소급 사유

    @Column(name = "overtime_retroactive_status", length = 30)
    private String overtimeRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)

}