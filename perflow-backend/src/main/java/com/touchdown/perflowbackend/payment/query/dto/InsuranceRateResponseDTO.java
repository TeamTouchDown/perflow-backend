package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class InsuranceRateResponseDTO {

    private final Long insuranceId;

    private final double nationalPensionRate;

    private final double healthInsuranceRate;

    private final double hireInsuranceRate;

    private final double longTermCareInsuranceRate;

}
