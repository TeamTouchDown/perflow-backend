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
@Table(name = "travel", schema = "perflow")
public class Travel {
    @Id
    @Column(name = "travel_id", nullable = false)
    private Long travelId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbj;

    @Column(name = "enroll_travel", nullable = false)
    private LocalDateTime enrollTravel;

    @Column(name = "travel_reason", nullable = false)
    private String travelReason;

    @Column(name = "travel_start", nullable = false)
    private LocalDateTime travelStart;

    @Column(name = "travel_end", nullable = false)
    private LocalDateTime travelEnd;

    @Column(name = "travel_status", nullable = false, length = 30)
    private String travelStatus;

    @Column(name = "travel_reject_reason")
    private String travelRejectReason;

    @Column(name = "travel_division", nullable = false, length = 30)
    private String travelDivision;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "status", nullable = false, length = 30)
    private Status status;

}