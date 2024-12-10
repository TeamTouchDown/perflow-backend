package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyRegisterRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.service.CompanyCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/company")
public class CompanyCommandController {

    private final CompanyCommandService companyCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> registerCompany(
            @RequestBody CompanyRegisterRequestDTO companyRegisterRequestDTO
    ){
        companyCommandService.registerCompany(companyRegisterRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
