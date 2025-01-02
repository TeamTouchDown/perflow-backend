package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.payment.command.application.dto.InsuranceRateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class InsuranceRateCommandController {

    private final InsuranceRateCommandService insuranceRateCommandService;

    @PostMapping("/insurance-rate")
    public ResponseEntity<String> createInsuranceRate(
            @RequestBody InsuranceRateRequestDTO request
            ) {

        insuranceRateCommandService.createInsuranceRate(request);

        return ResponseEntity.ok(SuccessCode.INSURANCE_RATE_SETTING_SUCCESS.getMessage());

    }
}
