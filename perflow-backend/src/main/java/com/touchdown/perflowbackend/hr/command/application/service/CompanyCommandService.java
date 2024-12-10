package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyRegisterRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyUpdateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.CompanyCommandMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.command.domain.repository.CompanyCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyCommandService {

    private final CompanyCommandRepository companyCommandRepository;
    private final CompanyCommandMapper companyCommandMapper;
    private final Long COMPANY_ID = 1L; // 회사 정보는 단 1개만 저장 될 예정

    @Transactional
    public void registerCompany(CompanyRegisterRequestDTO companyRegisterRequestDTO) {

        // 이미 회사 데이터가 존재하는지 확인
        if (isRegisteredCompany()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_COMPANY);
        }

        Company company = companyCommandMapper.toEntity(companyRegisterRequestDTO);

        companyCommandRepository.save(company);
    }

    @Transactional
    public void updateCompany(CompanyUpdateRequestDTO companyUpdateRequestDTO) {

        Company company = getCompany();

        company.updateCompany(companyUpdateRequestDTO);

        companyCommandRepository.save(company);
    }

    public boolean isRegisteredCompany() {
        return companyCommandRepository.existsById(COMPANY_ID);
    }

    public Company getCompany() {
        return companyCommandRepository.findById(COMPANY_ID).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_COMPANY)
        );
    }
}
