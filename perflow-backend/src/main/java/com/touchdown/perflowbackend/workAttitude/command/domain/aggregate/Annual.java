    package com.touchdown.perflowbackend.workAttitude.command.domain.aggregate;

    import com.touchdown.perflowbackend.common.BaseEntity;
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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "approver_id", nullable = false)
        private Employee approver; // 결재자 ID

        @Column(name = "enroll_annual", nullable = false)
        private LocalDateTime enrollAnnual;

        @Column(name = "annual_start", nullable = false)
        private LocalDateTime annualStart;

        @Column(name = "annual_end", nullable = false)
        private LocalDateTime annualEnd;

        @Column(name = "annual_status", nullable = false, length = 30)
        @Enumerated(EnumType.STRING)
        private AnnualType annualType;

        @Setter
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
                      Employee approver,
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
            this.approver = approver;
            this.enrollAnnual = enrollAnnual;
            this.annualStart = annualStart;
            this.annualEnd = annualEnd;
            this.annualType = annualType;
            this.annualRejectReason = annualRejectReason;
            this.isAnnualRetroactive = isAnnualRetroactive;
            this.annualRetroactiveReason = annualRetroactiveReason;
            this.annualRetroactiveStatus = annualRetroactiveStatus != null
                    ? annualRetroactiveStatus : AnnualRetroactiveStatus.NOTRETROACTIVATED; // 기본값 설정
            this.status = status != null
                    ? status : Status.ACTIVATED;
        }


        public void setUpdateDatetime(LocalDateTime now) {
            super.setUpdateDatetime(now);
        }

        public void setAnnualStatus(Status status) {
            this.status = status;
        }

        public void updateAnnual(LocalDateTime annualStart, LocalDateTime annualEnd, AnnualType annualType) {
            this.annualStart = annualStart;
            this.annualEnd = annualEnd;
            this.annualType = annualType;
        }

        public void rejectAnnual(String annualRejectReason) {
            this.annualRejectReason = annualRejectReason;
            this.status = Status.REJECTED;
        }

        public void updateRetroactive(Boolean isAnnualRetroactive, String annualRetroactiveReason, AnnualRetroactiveStatus annualRetroactiveStatus) {
            this.isAnnualRetroactive = isAnnualRetroactive;
            this.annualRetroactiveReason = annualRetroactiveReason;
            this.annualRetroactiveStatus = annualRetroactiveStatus;
        }

        public void softDelete() {
            this.status = Status.DELETED;
            this.setUpdateDatetime(LocalDateTime.now());
        }

    }
