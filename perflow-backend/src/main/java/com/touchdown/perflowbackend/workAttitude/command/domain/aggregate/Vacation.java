package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder // 빌더 어노테이션 추가
@Entity
@Table(name = "`vacation`", schema = "perflow")
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacation_id", nullable = false)
    private Long vacationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private Employee approver; // 결재자 ID

    @Column(name = "enroll_vacation", nullable = false)
    private LocalDateTime enrollVacation;

    @Column(name = "vacation_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private VacationType vacationType;

    @Column(name = "vacation_start", nullable = false)
    private LocalDateTime vacationStart;

    @Column(name = "vacation_end", nullable = false)
    private LocalDateTime vacationEnd;

    @Column(name = "vacation_status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private VacationStatus vacationStatus = VacationStatus.PENDING;

    @Column(name = "vacation_reject_reason")
    private String vacationRejectReason;

    @Setter
    @ColumnDefault("'ACTIVATED'")
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

    // 빌더 패턴용 생성자
    @Builder
    public Vacation(Employee empId,
                    Employee approver,
                    LocalDateTime enrollVacation,
                    LocalDateTime vacationStart,
                    LocalDateTime vacationEnd,
                    VacationType vacationType,
                    String vacationRejectReason,
                    VacationStatus vacationStatus,
                    Status status) {
        this.empId = empId;
        this.approver = approver;
        this.enrollVacation = enrollVacation;
        this.vacationStart = vacationStart;
        this.vacationEnd = vacationEnd;
        this.vacationType = vacationType;
        this.vacationRejectReason = vacationRejectReason;
        this.vacationStatus = vacationStatus != null ? vacationStatus : VacationStatus.PENDING;
        this.status = status != null ? status : Status.ACTIVATED; // 기본값 설정
    }


    // 휴가 정보 업데이트 메서드
    public void updateVacation(LocalDateTime start, LocalDateTime end, VacationType type) {
        this.vacationStart = start;
        this.vacationEnd = end;
        this.vacationType = type;
    }
    public void updateApprover(Employee approver) {
        if (approver == null) {
            throw new IllegalArgumentException("결재자는 null일 수 없습니다.");
        }
        this.approver = approver;
    }
    public void updateVacationStatus(VacationStatus status, String rejectReason) {
        this.vacationStatus = status;
        if (status == VacationStatus.REJECTED) {
            this.vacationRejectReason = rejectReason;
        } else {
            this.vacationRejectReason = null; // 승인 시 반려 사유 초기화
        }
    }


    // 상태 업데이트 메서드
    public void updateStatus(VacationStatus status) {
        this.vacationStatus = status;
        this.setUpdateDatetime(LocalDateTime.now()); // 업데이트 시간 동기화
    }


    // 휴가 반려 메서드
    public void rejectVacation(String reason) {
        this.vacationRejectReason = reason;
        this.vacationStatus = VacationStatus.REJECTED;
    }

    // 휴가 승인 메서드
    public void approveVacation() {
        this.vacationStatus = VacationStatus.CONFIRMED;
    }

    // 휴가 소프트 삭제 처리
    public void softDelete() {
        this.status = Status.DELETED;
    }
}
