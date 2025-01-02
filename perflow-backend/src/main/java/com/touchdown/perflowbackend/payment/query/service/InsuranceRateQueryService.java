package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;
import com.touchdown.perflowbackend.payment.query.dto.InsuranceRateResponseDTO;
import com.touchdown.perflowbackend.payment.query.repository.InsuranceRateQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceRateQueryService {

    private final InsuranceRateQueryRepository insuranceRateQueryRepository;

    public InsuranceRateResponseDTO getInsuranceRate(Long insuranceRateId) {

        InsuranceRate insuranceRate = insuranceRateQueryRepository.findByInsuranceRateId(insuranceRateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_INSURANCE_RATE));

        return InsuranceRateResponseDTO.builder()
                .insuranceId(insuranceRate.getInsuranceRateId())
                .nationalPensionRate(insuranceRate.getNationalPensionRate())
                .healthInsuranceRate(insuranceRate.getHealthInsuranceRate())
                .hireInsuranceRate(insuranceRate.getHireInsuranceRate())
                .longTermCareInsuranceRate(insuranceRate.getLongTermCareInsuranceRate())
                .build();

    }

}
