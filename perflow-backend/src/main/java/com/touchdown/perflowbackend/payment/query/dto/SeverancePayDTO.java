package com.touchdown.perflowbackend.payment.query.dto;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SeverancePayDTO {

    private final Long severancePayId;

    private final String empId;

    private final String empName;

    private final LocalDate joinDate;

    private final LocalDate resignDate;

    private final String positionName;

    private final String deptName;

    private final Long threeMonthTotalPay;

    private final Long threeMonthTotalDays;

    private final Long threeMonthTotalAllowance;

    private final Long totalLaborDays;

    private final Long totalSeverancePay;

    private final Status severanceStatus;

    private final LocalDateTime createDatetime;

}
