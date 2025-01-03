package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.payment.command.application.dto.InsuranceRateRequestDTO;
import com.touchdown.perflowbackend.payment.command.application.service.InsuranceRateCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/insurance-rate/{insuranceRateId}")
    public ResponseEntity<String> updateInsuranceRate(
            @PathVariable Long insuranceRateId,
            @RequestBody InsuranceRateRequestDTO request
    ) {

        insuranceRateCommandService.updateInsuranceRate(insuranceRateId, request);

        return ResponseEntity.ok(SuccessCode.INSURANCE_RATE_UPDATE_SUCCESS.getMessage());
    }
}
