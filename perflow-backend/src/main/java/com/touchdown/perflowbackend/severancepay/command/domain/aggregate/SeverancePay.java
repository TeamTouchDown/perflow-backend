package com.touchdown.perflowbackend.severancepay.command.domain.aggregate;

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
@Table(name = "severance_pay", schema = "perflow")
public class SeverancePay {
    @Id
    @Column(name = "severance_pay_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee emp;

    @Column(name = "severance_pay", nullable = false)
    private Long severancePay;

    @Column(name = "extend_labor_allowence")
    private Long extendLaborAllowence;

    @Column(name = "night_labor_allowence")
    private Long nightLaborAllowence;

    @Column(name = "holiday_labor_allowence")
    private Long holidayLaborAllowence;

    @Column(name = "annual_allowence")
    private Long annualAllowence;

    @Column(name = "severance_day", nullable = false)
    private LocalDate severanceDay;

    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "payment_datetime", nullable = false)
    private Instant paymentDatetime;

    @Column(name = "create_datetime", nullable = false)
    private Instant createDatetime;

}