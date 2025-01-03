package com.touchdown.perflowbackend.payment.query.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class PayrollDTO {

    private final Long payrollId;

    private final String empId;

    private final String empName;

//    private final String img;

    private final String deptName;

    private final EmployeeStatus empStatus;

    private final Long pay;

    private final Long extendLaborAllowance;

    private final Long nightLaborAllowance;

    private final Long holidayLaborAllowance;

    private final Long annualAllowance;

    private final Long incentive;

    private final Long totalPayment;

    private final Long nationalPension;

    private final Long healthInsurance;

    private final Long hireInsurance;

    private final Long longTermCareInsurance;

    private final Long incomeTax;

    private final Long localIncomeTax;

    private final Long totalDeduction;

    private final Long totalAmount;

    private final Status payrollStatus;

    private final LocalDateTime createDatetime;

}
