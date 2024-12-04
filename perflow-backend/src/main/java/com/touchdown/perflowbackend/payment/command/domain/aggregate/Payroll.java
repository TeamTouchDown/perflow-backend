package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "payroll", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payroll extends BaseEntity {

    @Id
    @Column(name = "payroll_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

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

    @Column(name = "total_start_date", nullable = false)
    private LocalDate totalStartDate;

    @Column(name = "total_end_date", nullable = false)
    private LocalDate totalEndDate;

}