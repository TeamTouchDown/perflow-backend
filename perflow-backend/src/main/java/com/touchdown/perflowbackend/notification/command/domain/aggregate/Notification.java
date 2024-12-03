package com.touchdown.perflowbackend.notification.command.domain.aggregate;

import com.touchdown.perflowbackend.hrperfo.command.domain.aggregate.HrPerfo;
import com.touchdown.perflowbackend.leave.command.domain.aggregate.Leave;
import com.touchdown.perflowbackend.annual.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.appoint.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.overtime.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.payroll.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.travel.command.domain.aggregate.Travel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "perflow")
public class Notification {
    @Id
    @Column(name = "noti_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approve_sbj_id")
    private ApproveSbj approveSbj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_perfo_id")
    private HrPerfo hrPerfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointId")
    private Appoint appoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overtime_id")
    private Overtime overtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annual_id")
    private Annual annual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id")
    private Leave leave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id")
    private Payroll payroll;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "url", nullable = false)
    private String url;

    @ColumnDefault("0")
    @Column(name = "status", nullable = false)
    private Byte status;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

}