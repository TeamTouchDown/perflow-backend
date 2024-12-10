package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyAnnualCountUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyPaymentDatetimeUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyRegisterRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyUpdateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.CompanyCommandMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.command.domain.repository.CompanyCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Transactional
    public void updateAnnualCount(CompanyAnnualCountUpdateDTO companyAnnualCountUpdateDTO) {

        // 연차가 12일 이상인지 확인
        if(!isAnnualOver12(companyAnnualCountUpdateDTO.getCompanyAnnualCount())) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_ANNUAL);
        }

        Company company = getCompany();

        company.updateAnnualCount(companyAnnualCountUpdateDTO);

        companyCommandRepository.save(company);
    }

    public void updatePaymentDateTime(CompanyPaymentDatetimeUpdateDTO companyPaymentDatetimeUpdateDTO) {

        if(!isBetween1And28(companyPaymentDatetimeUpdateDTO.getDate())){
            throw new CustomException(ErrorCode.NOT_MATCHED_PAYMENT_DATE);
        }

        Company company = getCompany();

        company.updatePaymentDatetime(companyPaymentDatetimeUpdateDTO);

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

    // 연차 12일 이상인지
    public boolean isAnnualOver12(Integer annualCount) {
        return annualCount >= 12;
    }

    // 급여 지급일 1~28일 사이인지
    public boolean isBetween1And28(Integer annualCount) {
        return annualCount >= 1 && annualCount <= 28;
    }

}
