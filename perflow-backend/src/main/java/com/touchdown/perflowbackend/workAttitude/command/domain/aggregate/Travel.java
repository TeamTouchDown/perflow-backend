package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private Employee approver; // 결재자 ID

    @Column(name = "enroll_travel", nullable = false)
    private LocalDateTime enrollTravel;

    @Column(name = "travel_reason", nullable = false)
    private String travelReason;

    @Column(name = "travel_start", nullable = false)
    private LocalDateTime travelStart;

    @Column(name = "travel_end", nullable = false)
    private LocalDateTime travelEnd;

    @Enumerated(EnumType.STRING)
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
    public Travel(Employee empId,
                  Employee approver,
                  LocalDateTime enrollTravel,
                  String travelReason,
                  LocalDateTime travelStart,
                  LocalDateTime travelEnd,
                  Status travelStatus,
                  String travelRejectReason,
                  String travelDivision,
                  Status status) {
        this.employee = empId;
        this.approver = approver;
        this.enrollTravel = enrollTravel;
        this.travelReason = travelReason;
        this.travelStart = travelStart;
        this.travelEnd = travelEnd;
        this.travelStatus = travelStatus;
        this.travelRejectReason = travelRejectReason;
        this.travelDivision = travelDivision;
        this.status = status;
    }


    public void updateTravel(String travelReason, LocalDateTime travelStart, LocalDateTime travelEnd, String travelDivision) {
        if (travelReason != null) this.travelReason = travelReason;
        if (travelStart != null) this.travelStart = travelStart;
        if (travelEnd != null) this.travelEnd = travelEnd;
        if (travelDivision != null) this.travelDivision = travelDivision;
    }

    public void updateTravelStatus(Status travelStatus, String travelRejectReason) {
        this.travelStatus = travelStatus;
        if (travelStatus == Status.REJECTED) {
            this.travelRejectReason = travelRejectReason;
        } else {
            this.travelRejectReason = null; // 승인일 경우 반려 사유를 초기화
        }
    }

    public void deleteTravel() {
        this.status = Status.DELETED;
    }
}