package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyAnnualCountUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyPaymentDatetimeUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyCreateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyUpdateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.service.CompanyCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/company")
public class CompanyCommandController {

    private final CompanyCommandService companyCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> createCompany(
            @RequestBody CompanyCreateRequestDTO companyCreateRequestDTO
    ) {
        companyCommandService.createCompany(companyCreateRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @PutMapping
    public ResponseEntity<SuccessCode> updateCompanyDetail(
            @RequestBody CompanyUpdateRequestDTO companyUpdateRequestDTO
    ) {
        companyCommandService.updateCompany(companyUpdateRequestDTO);

        return ResponseEntity.ok(SuccessCode.COMPANY_UPDATE_SUCCESS);
    }

    // 연차 개수 수정
    @PutMapping("/annualCount")
    public ResponseEntity<SuccessCode> updateCompanyAnnualCount(
            @RequestBody CompanyAnnualCountUpdateDTO companyAnnualCountUpdateDTO
    ) {
        companyCommandService.updateAnnualCount(companyAnnualCountUpdateDTO);

        return ResponseEntity.ok(SuccessCode.COMPANY_UPDATE_SUCCESS);
    }

    // 급여 지급일 수정
    @PutMapping("/paymentDatetime")
    public ResponseEntity<SuccessCode> updateCompanyPaymentDatetime(
            @RequestBody CompanyPaymentDatetimeUpdateDTO companyPaymentDatetimeUpdateDTO
    ) {
        companyCommandService.updatePaymentDateTime(companyPaymentDatetimeUpdateDTO);

        return ResponseEntity.ok(SuccessCode.COMPANY_UPDATE_SUCCESS);
    }

}
