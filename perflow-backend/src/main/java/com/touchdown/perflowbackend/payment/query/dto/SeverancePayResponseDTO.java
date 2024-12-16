package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
public class SeverancePayResponseDTO {

    private final Long severancePayId;

    private final String name;

    private final LocalDate createDatetime;

    private final Long totalEmp;

    private final Long totalPay;

}
