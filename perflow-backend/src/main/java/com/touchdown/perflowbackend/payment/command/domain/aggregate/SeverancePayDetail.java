package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "severance_pay_detail", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeverancePayDetail extends BaseEntity {

    @Id
    @Column(name = "severance_pay_detail_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long severancePayDetailId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "severance_pay_id", nullable = false)
    private SeverancePay severancePay;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "extend_labor_allowance")
    private Long extendLaborAllowance;

    @Column(name = "night_labor_allowance")
    private Long nightLaborAllowance;

    @Column(name = "holiday_labor_allowance")
    private Long holidayLaborAllowance;

    @Column(name = "annual_allowance")
    private Long annualAllowance;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.PENDING;

    // SeverancePay 객체를 설정하는 메서드 (setter 대신)
    public void assignSeverancePay(SeverancePay severancePay) {

        this.severancePay = severancePay;

    }

    public SeverancePayDetail(Employee emp, Long extendLaborAllowance, Long nightLaborAllowance, Long holidayLaborAllowance,
                         Long annualAllowance, Long totalAmount) {

        this.emp = emp;
        this.extendLaborAllowance = extendLaborAllowance;
        this.nightLaborAllowance = nightLaborAllowance;
        this.holidayLaborAllowance = holidayLaborAllowance;
        this.annualAllowance = annualAllowance;
        this.totalAmount = totalAmount;

    }

    public void updateSeverancePay(Long extendLaborAllowance, Long nightLaborAllowance,
                              Long holidayLaborAllowance, Long annualAllowance, Long totalAmount) {

        this.extendLaborAllowance = extendLaborAllowance;
        this.nightLaborAllowance = nightLaborAllowance;
        this.holidayLaborAllowance = holidayLaborAllowance;
        this.annualAllowance = annualAllowance;
        this.totalAmount = totalAmount;
        this.status = Status.UPDATED;

    }

    public void completeSeverancePay() {

        this.status = Status.COMPLETE;

    }
}