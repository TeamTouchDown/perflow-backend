package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import jakarta.persistence.*;
import lombok.*;

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

    @Setter
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
    private Status travelStatus = Status.PENDING;;

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
        if (travelEnd.isBefore(travelStart)) {
            throw new IllegalArgumentException("출장 종료일이 시작일보다 앞에 있을 수 없습니다.");
        }

        this.employee = empId;
        this.approver = approver;
        this.enrollTravel = enrollTravel;
        this.travelReason = travelReason;
        this.travelStart = travelStart;
        this.travelEnd = travelEnd;
        this.travelStatus = travelStatus != null ? travelStatus : Status.PENDING;
        this.travelRejectReason = travelRejectReason;
        this.travelDivision = travelDivision;
        this.status = status!= null ? status : Status.ACTIVATED;
    }


    public void updateTravel(String travelReason, LocalDateTime travelStart, LocalDateTime travelEnd, String travelDivision) {
        if (travelStart != null && travelEnd != null && travelEnd.isBefore(travelStart)) {
            throw new IllegalArgumentException("출장 종료일이 시작일보다 앞에 있을 수 없습니다.");
        }
        this.travelReason = travelReason != null ? travelReason : this.travelReason;
        this.travelStart = travelStart != null ? travelStart : this.travelStart;
        this.travelEnd = travelEnd != null ? travelEnd : this.travelEnd;
        this.travelDivision = travelDivision != null ? travelDivision : this.travelDivision;
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

    public void updateApprover(Employee approver) {
        if (approver == null) {
            throw new IllegalArgumentException("결재자를 반드시 입력해야 합니다.");
        }
        this.approver = approver;
    }


}