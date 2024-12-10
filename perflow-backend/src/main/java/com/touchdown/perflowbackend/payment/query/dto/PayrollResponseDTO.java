package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
public class PayrollResponseDTO {

    private final Long payrollId;
    private final String name;
    private final LocalDate createDatetime;
    private final Long totalEmp;
    private final Long totalPay;

}
