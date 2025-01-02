package com.touchdown.perflowbackend.payment.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class InsuranceRateRequestDTO {

    private final Long insuranceId;

    private final Long nationalPensionRate;

    private final Long healthInsuranceRate;

    private final Long hireInsuranceRate;

    private final Long longTermCareInsuranceRate;

}
