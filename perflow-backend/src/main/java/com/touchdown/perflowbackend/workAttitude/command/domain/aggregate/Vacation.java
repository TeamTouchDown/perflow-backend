package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`vacation`", schema = "perflow")
public class Vacation {
    @Id
    @Column(name = "vacation_id", nullable = false)
    private Long vacationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbjId;

    @Column(name = "enroll_vacation", nullable = false)
    private LocalDateTime enrollVacation;

    @Column(name = "vacation_type", nullable = false, length = 30)
    private String vacationType;

    @Column(name = "vacation_start", nullable = false)
    private LocalDateTime vacationStart;

    @Column(name = "vacation_end", nullable = false)
    private LocalDateTime vacationEnd;

    @Column(name = "vacation_status", nullable = false, length = 30)
    private String vacationStatus;

    @Column(name = "vacation_reject_reason")
    private String vacationRejectReason;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

}