package com.touchdown.perflowbackend.payroll.command.domain.aggregate;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payroll", schema = "perflow")
public class Payroll {
    @Id
    @Column(name = "payroll_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "extend_labor_allowence")
    private Long extendLaborAllowence;

    @Column(name = "night_labor_allowence")
    private Long nightLaborAllowence;

    @Column(name = "holiday_labor_allowence")
    private Long holidayLaborAllowence;

    @Column(name = "annual_allowence")
    private Long annualAllowence;

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

    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "total_start_date", nullable = false)
    private LocalDate totalStartDate;

    @Column(name = "total_end_date", nullable = false)
    private LocalDate totalEndDate;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

}