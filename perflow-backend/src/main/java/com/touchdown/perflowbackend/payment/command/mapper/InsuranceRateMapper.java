package com.touchdown.perflowbackend.payment.command.mapper;

import com.touchdown.perflowbackend.payment.command.application.dto.InsuranceRateRequestDTO;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;

public class InsuranceRateMapper {

    public static InsuranceRate toEntity(InsuranceRateRequestDTO request) {

        return InsuranceRate.builder()
                .nationalPensionRate(request.getNationalPensionRate())
                .healthInsuranceRate(request.getHealthInsuranceRate())
                .hireInsuranceRate(request.getHireInsuranceRate())
                .longTermCareInsuranceRate(request.getLongTermCareInsuranceRate())
                .build();
    }
}
