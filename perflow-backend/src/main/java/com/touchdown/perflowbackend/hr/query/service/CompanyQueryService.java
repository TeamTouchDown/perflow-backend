package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.mapper.CompanyMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.query.dto.CompanyResponseDTO;
import com.touchdown.perflowbackend.hr.query.repository.CompanyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyQueryService {

    private final Long COMPANY_ID = 1L;
    private final CompanyQueryRepository companyQueryRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyResponseDTO getCompanyResponse() {

        Company company = companyQueryRepository.findById(COMPANY_ID).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_COMPANY)
        );

        return companyMapper.toResponse(company);
    }
}
