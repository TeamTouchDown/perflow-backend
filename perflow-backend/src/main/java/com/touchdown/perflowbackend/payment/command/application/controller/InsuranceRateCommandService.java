package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.payment.command.application.dto.InsuranceRateRequestDTO;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;
import com.touchdown.perflowbackend.payment.command.domain.repository.InsuranceRateRepository;
import com.touchdown.perflowbackend.payment.command.mapper.InsuranceRateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsuranceRateCommandService {

    private final InsuranceRateRepository insuranceRateRepository;

    @Transactional
    public void createInsuranceRate(InsuranceRateRequestDTO request) {

        InsuranceRate newInsuranceRate = InsuranceRateMapper.toEntity(request);

        insuranceRateRepository.save(newInsuranceRate);

    }
}
