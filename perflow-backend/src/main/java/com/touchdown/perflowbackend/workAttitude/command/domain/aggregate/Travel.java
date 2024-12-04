package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.common.BaseEntity;
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
@Table(name = "travel", schema = "perflow")
public class Travel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    private Long travelId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

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
    private Status travelStatus;

    @Column(name = "travel_reject_reason")
    private String travelRejectReason;

    @Column(name = "travel_division", nullable = false, length = 30)
    private String travelDivision;


    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Travel(Employee empId, String travelReason, LocalDateTime travelStart, LocalDateTime travelEnd, String travelDivision, String travelStatus, LocalDateTime createdAt) {
        this.employee = empId;
        this.travelReason = travelReason;
        this.travelStart = travelStart;
        this.travelEnd = travelEnd;
        this.travelDivision = travelDivision;
        this.travelStatus = Status.valueOf(travelStatus);
    }
}