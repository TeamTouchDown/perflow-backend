package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PayrollChartDTO {

    private final Long payrollId;

    private final Long totalAmount;

    private final LocalDateTime createDatetime;

}
