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
    @Entity
    @Table(name = "annual", schema = "perflow")
    public class Annual extends BaseEntity { //연차

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "annual_id", nullable = false)
        private Long annualId;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "emp_id", nullable = false)
        private Employee empId;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "approve_sbj_id", nullable = false)
        private ApproveSbj approveSbjId;

        @Column(name = "enroll_annual", nullable = false)
        private LocalDateTime enrollAnnual;

        @Column(name = "annual_start", nullable = false)
        private LocalDateTime annualStart;

        @Column(name = "annual_end", nullable = false)
        private LocalDateTime annualEnd;

        @Column(name = "annual_status", nullable = false, length = 30)
        @Enumerated(EnumType.STRING)
        private AnnualType annualType;

        @Column(name = "annual_reject_reason")
        private String annualRejectReason;

        @Setter
        @ColumnDefault("'ACTIVE'")
        @Column(name = "status", nullable = false, length = 30)
        @Enumerated(EnumType.STRING)
        private Status status;

        @ColumnDefault("0")
        @Column(name = "is_annual_retroactive", nullable = false)
        private Boolean isAnnualRetroactive = false; // 소급 여부 (0: 일반, 1: 소급)

        @Column(name = "annual_retroactive_reason", length = 255)
        private String annualRetroactiveReason; // 소급 사유

        @Column(name = "annual_retroactive_status", length = 30)
        @Enumerated(EnumType.STRING)
        private AnnualRetroactiveStatus annualRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)



        // Annual 엔티티의 일부 추가 코드
        @Builder
        public Annual(Employee empId,
                      ApproveSbj approveSbjId,
                      LocalDateTime enrollAnnual,
                      LocalDateTime annualStart,
                      LocalDateTime annualEnd,
                      AnnualType annualType,
                      String annualRejectReason,
                      Boolean isAnnualRetroactive,
                      String annualRetroactiveReason,
                      AnnualRetroactiveStatus annualRetroactiveStatus,
                      Status status) {
            this.empId = empId;
            this.approveSbjId = approveSbjId;
            this.enrollAnnual = enrollAnnual;
            this.annualStart = annualStart;
            this.annualEnd = annualEnd;
            this.annualType = annualType;
            this.annualRejectReason = annualRejectReason;
            this.isAnnualRetroactive = isAnnualRetroactive;
            this.annualRetroactiveReason = annualRetroactiveReason;
            this.annualRetroactiveStatus = annualRetroactiveStatus;
            this.status = Status.ACTIVATED; // 기본값 설정
        }

        // 연차 종료일 업데이트 메서드
        public void updateAnnual(LocalDateTime annualStart,
                                 LocalDateTime annualEnd,
                                 AnnualType annualType) {
            this.annualStart = annualStart;
            this.annualEnd = annualEnd;
            this.annualType = annualType;
        }

        // 연차 반려 사유 추가 메서드
        public void rejectAnnual(String rejectReason) {
            this.status = Status.REJECTED; // 상태 업데이트
            this.annualRejectReason = rejectReason;
        }

        // 연차 승인 메서드
        public void approveAnnual() {
            this.status = Status.CONFIRMED; // 상태 업데이트
        }


        // 소급 신청 상태 업데이트 메서드
        public void updateRetroactive(Boolean isRetroactive,
                                      String retroactiveReason,
                                      AnnualRetroactiveStatus retroactiveStatus) {
            this.isAnnualRetroactive = isRetroactive;
            this.annualRetroactiveReason = retroactiveReason;
            this.annualRetroactiveStatus = retroactiveStatus;
        }

        // 연차 상태 삭제 처리 (소프트 삭제)
        public void softDelete() {
            this.status = Status.DELETED;
        }

    }
