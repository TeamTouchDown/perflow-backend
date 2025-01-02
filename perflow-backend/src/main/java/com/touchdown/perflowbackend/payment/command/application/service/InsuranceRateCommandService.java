package com.touchdown.perflowbackend.payment.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.command.application.dto.InsuranceRateRequestDTO;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;
import com.touchdown.perflowbackend.payment.command.domain.repository.InsuranceRateCommandRepository;
import com.touchdown.perflowbackend.payment.command.mapper.InsuranceRateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsuranceRateCommandService {

    private final InsuranceRateCommandRepository insuranceRateCommandRepository;

    @Transactional
    public void createInsuranceRate(InsuranceRateRequestDTO request) {

        InsuranceRate newInsuranceRate = InsuranceRateMapper.toEntity(request);

        insuranceRateCommandRepository.save(newInsuranceRate);

    }

    @Transactional
    public void updateInsuranceRate(Long insuranceRateId, InsuranceRateRequestDTO request) {

        InsuranceRate insuranceRate = insuranceRateCommandRepository.findByInsuranceRateId(insuranceRateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_INSURANCE_RATE));

        insuranceRate.updateInsuranceRate(
                request.getNationalPensionRate(),
                request.getHealthInsuranceRate(),
                request.getHireInsuranceRate(),
                request.getLongTermCareInsuranceRate()
        );
    }
}
