package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.payment.query.dto.InsuranceRateResponseDTO;
import com.touchdown.perflowbackend.payment.query.service.InsuranceRateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class InsuranceRateQueryController {

    private final InsuranceRateQueryService insuranceRateQueryService;

    @GetMapping("/insurance-rate/{insuranceRateId}")
    public ResponseEntity<InsuranceRateResponseDTO> getInsuranceRate(@PathVariable Long insuranceRateId) {

        InsuranceRateResponseDTO response = insuranceRateQueryService.getInsuranceRate(insuranceRateId);

        return ResponseEntity.ok(response);

    }

}
