package com.touchdown.perflowbackend.payment.command.domain.aggregate;

import com.touchdown.perflowbackend.common.BaseEntity;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "severance_pay", schema = "perflow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeverancePay extends BaseEntity {

    @Id
    @Column(name = "severance_pay_id", nullable = false)
    private Long severancePayId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee empId;

    @Column(name = "severance_pay", nullable = false)
    private Long severancePay;

    @Column(name = "extend_labor_allowance")
    private Long extendLaborAllowance;

    @Column(name = "night_labor_allowance")
    private Long nightLaborAllowance;

    @Column(name = "holiday_labor_allowance")
    private Long holidayLaborAllowance;

    @Column(name = "annual_allowance")
    private Long annualAllowance;

    @Column(name = "severance_day", nullable = false)
    private LocalDate severanceDay;

    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.PENDING;

    @Column(name = "payment_datetime", nullable = false)
    private LocalDateTime paymentDatetime;

}