package com.touchdown.perflowbackend.leave.command.domain.aggregate;

import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "`leave`", schema = "perflow")
public class Leave {
    @Id
    @Column(name = "leave_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbj;

    @Column(name = "enroll_leave", nullable = false)
    private Instant enrollLeave;

    @Column(name = "leave_type", nullable = false, length = 30)
    private String leaveType;

    @Column(name = "leave_start", nullable = false)
    private Instant leaveStart;

    @Column(name = "leave_end", nullable = false)
    private Instant leaveEnd;

    @Column(name = "leave_status", nullable = false, length = 30)
    private String leaveStatus;

    @Column(name = "leave_reject_reason")
    private String leaveRejectReason;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

}