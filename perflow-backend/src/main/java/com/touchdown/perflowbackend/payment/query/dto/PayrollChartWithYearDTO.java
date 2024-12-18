package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PayrollChartWithYearDTO {

    private final Long payrollId;

    private final int year;

    private final Long totalAmount;

}
