package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.CompanyResponseDTO;
import com.touchdown.perflowbackend.hr.query.service.CompanyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyQueryController {

    private final CompanyQueryService companyQueryService;

    @GetMapping
    public ResponseEntity<CompanyResponseDTO> getCompany() {

        CompanyResponseDTO responseDTO = companyQueryService.getCompanyResponse();

        return ResponseEntity.ok(responseDTO);
    }
}
