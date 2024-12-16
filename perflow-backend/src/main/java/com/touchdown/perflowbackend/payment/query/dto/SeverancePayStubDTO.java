package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
public class SeverancePayStubDTO {

    private final String empId;

    private final String empName;

    private final LocalDate joinDate;

    private final LocalDate resignDate;

    private final String deptName;

    private final String positionName;

    private final Long totalLaborDays;

    private final Long totalPay;

    private final Long totalAllowance;

    private final Long totalAnnulAllowance;

    private final Long totalOverTimeAllowance;

    private final Long totalExtendLaborAllowance;

    private final Long totalNightLaborAllowance;

    private final Long totalHolidayLaborAllowance;

    private final Long totalAmount;

}
