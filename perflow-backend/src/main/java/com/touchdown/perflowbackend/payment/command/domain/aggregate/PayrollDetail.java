package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payroll_detail", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayrollDetail extends BaseEntity {

    @Id
    @Column(name = "payroll_detail_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollDetailId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "extend_labor_allowance")
    private Long extendLaborAllowance;

    @Column(name = "night_labor_allowance")
    private Long nightLaborAllowance;

    @Column(name = "holiday_labor_allowance")
    private Long holidayLaborAllowance;

    @Column(name = "annual_allowance")
    private Long annualAllowance;

    @Column(name = "incentive")
    private Long incentive;

    @Column(name = "national_pension", nullable = false)
    private Long nationalPension;

    @Column(name = "health_insurance", nullable = false)
    private Long healthInsurance;

    @Column(name = "hire_insurance", nullable = false)
    private Long hireInsurance;

    @Column(name = "long_term_care_insurance", nullable = false)
    private Long longTermCareInsurance;

    @Column(name = "income_tax", nullable = false)
    private Long incomeTax;

    @Column(name = "local_income_tax", nullable = false)
    private Long localIncomeTax;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.PENDING;

    // Payroll 객체를 설정하는 메서드 (setter 대신)
    public void assignPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public PayrollDetail(Employee emp, Long extendLaborAllowance, Long nightLaborAllowance, Long holidayLaborAllowance,
                         Long annualAllowance, Long incentive, Long nationalPension, Long healthInsurance, Long hireInsurance,
                         Long longTermCareInsurance, Long incomeTax, Long localIncomeTax, Long totalAmount) {

        this.emp = emp;
        this.extendLaborAllowance = extendLaborAllowance;
        this.nightLaborAllowance = nightLaborAllowance;
        this.holidayLaborAllowance = holidayLaborAllowance;
        this.annualAllowance = annualAllowance;
        this.incentive = incentive;
        this.nationalPension = nationalPension;
        this.healthInsurance = healthInsurance;
        this.hireInsurance = hireInsurance;
        this.longTermCareInsurance = longTermCareInsurance;
        this.incomeTax = incomeTax;
        this.localIncomeTax = localIncomeTax;
        this.totalAmount = totalAmount;

    }

    public void updatePayroll(Long extendLaborAllowance, Long nightLaborAllowance,
                                      Long holidayLaborAllowance, Long annualAllowance, Long incentive,
                                      Long nationalPension, Long healthInsurance, Long hireInsurance,
                                      Long longTermCareInsurance, Long incomeTax, Long localIncomeTax,
                                      Long totalAmount) {

            this.extendLaborAllowance = extendLaborAllowance;
            this.nightLaborAllowance = nightLaborAllowance;
            this.holidayLaborAllowance = holidayLaborAllowance;
            this.annualAllowance = annualAllowance;
            this.incentive = incentive;
            this.nationalPension = nationalPension;
            this.healthInsurance = healthInsurance;
            this.hireInsurance = hireInsurance;
            this.longTermCareInsurance = longTermCareInsurance;
            this.incomeTax = incomeTax;
            this.localIncomeTax = localIncomeTax;
            this.totalAmount = totalAmount;
            this.status = Status.UPDATED;

    }

    public void completePayroll() {

        this.status = Status.COMPLETE;

    }

}