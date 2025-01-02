package com.touchdown.perflowbackend.payment.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class InsuranceRateRequestDTO {

    private final Long insuranceId;

    private final double nationalPensionRate;

    private final double healthInsuranceRate;

    private final double hireInsuranceRate;

    private final double longTermCareInsuranceRate;

}
