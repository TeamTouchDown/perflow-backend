package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "overtime", schema = "perflow")
public class Overtime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "overtime_id", nullable = false)
    private Long overtimeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbjId;

    @Column(name = "overtime_type", nullable = false, length = 30)
    private String overtimeType;

    @Column(name = "enroll_overtime", nullable = false)
    private LocalDateTime enrollOvertime;

    @Column(name = "overtime_start", nullable = false)
    private LocalDateTime overtimeStart;

    @Column(name = "overtime_end", nullable = false)
    private LocalDateTime overtimeEnd;

    @Column(name = "overtime_status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status overtimeStatus;

    @Column(name = "travel_reject_time")
    private String travelRejectTime;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @Column(name = "is_overtime_retroactive", nullable = false)
    private Boolean isOvertimeRetroactive = false; // 소급 여부 (0: 일반, 1: 소급)

    @Column(name = "overtime_retroactive_reason", length = 255)
    private String overtimeRetroactiveReason; // 소급 사유

    @Column(name = "overtime_retroactive_status", length = 30)
    @Enumerated(EnumType.STRING)
    private Status overtimeRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)

    @Builder
    public Overtime(Employee empId, ApproveSbj approveSbjId, String overtimeType, LocalDateTime enrollOvertime,
                    LocalDateTime overtimeStart, LocalDateTime overtimeEnd, Status overtimeStatus,
                    String travelRejectTime, Boolean isOvertimeRetroactive, String overtimeRetroactiveReason,
                    Status overtimeRetroactiveStatus, Status status) {
        this.empId = empId;
        this.approveSbjId = approveSbjId;
        this.overtimeType = overtimeType;
        this.enrollOvertime = enrollOvertime;
        this.overtimeStart = overtimeStart;
        this.overtimeEnd = overtimeEnd;
        this.overtimeStatus = overtimeStatus;
        this.travelRejectTime = travelRejectTime;
        this.isOvertimeRetroactive = isOvertimeRetroactive;
        this.overtimeRetroactiveReason = overtimeRetroactiveReason;
        this.overtimeRetroactiveStatus = overtimeRetroactiveStatus;
        this.status = status;
    }
}