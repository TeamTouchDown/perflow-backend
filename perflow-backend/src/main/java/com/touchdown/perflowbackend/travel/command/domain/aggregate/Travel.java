package com.touchdown.perflowbackend.travel.command.domain.aggregate;

import com.touchdown.perflowbackend.approvesbj.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "travel", schema = "perflow")
public class Travel {
    @Id
    @Column(name = "travel_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbj;

    @Column(name = "enroll_travel", nullable = false)
    private Instant enrollTravel;

    @Column(name = "travel_reson", nullable = false)
    private String travelReson;

    @Column(name = "travel_start", nullable = false)
    private Instant travelStart;

    @Column(name = "travel_end", nullable = false)
    private Instant travelEnd;

    @Column(name = "travel_status", nullable = false, length = 30)
    private String travelStatus;

    @Column(name = "travel_reject_reason")
    private String travelRejectReason;

    @Column(name = "travel_division", nullable = false, length = 30)
    private String travelDivision;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

}