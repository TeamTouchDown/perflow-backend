package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approve_sbj_id", nullable = false)
    private ApproveSbj approveSbjId;

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
    private VacationStatus vacationStatus;

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
                    ApproveSbj approveSbjId,
                    LocalDateTime enrollVacation,
                    LocalDateTime vacationStart,
                    LocalDateTime vacationEnd,
                    VacationType vacationType,
                    String vacationRejectReason,
                    VacationStatus vacationStatus,
                    Status status) {
        this.empId = empId;
        this.approveSbjId = approveSbjId;
        this.enrollVacation = enrollVacation;
        this.vacationStart = vacationStart;
        this.vacationEnd = vacationEnd;
        this.vacationType = vacationType;
        this.vacationRejectReason = vacationRejectReason;
        this.vacationStatus = vacationStatus;
        this.status = status != null ? status : Status.ACTIVATED; // 기본값 설정
    }


    // 휴가 정보 업데이트 메서드
    public void updateVacation(LocalDateTime start, LocalDateTime end, VacationType type) {
        this.vacationStart = start;
        this.vacationEnd = end;
        this.vacationType = type;
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
