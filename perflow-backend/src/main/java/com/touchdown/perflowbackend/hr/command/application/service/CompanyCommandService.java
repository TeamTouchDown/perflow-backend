package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.file.command.application.service.FileService;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyAnnualCountUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyPaymentDatetimeUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyCreateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyUpdateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.CompanyMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.command.domain.repository.CompanyCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyCommandService {

    private final CompanyCommandRepository companyCommandRepository;
    private final FileService fileService;
    private final CompanyMapper companyMapper;
    private final Long COMPANY_ID = 1L; // 회사 정보는 단 1개만 저장 될 예정

    @Transactional
    public void createCompany(CompanyCreateRequestDTO companyCreateRequestDTO) {

        // 이미 회사 데이터가 존재하는지 확인
        if (isCreatedCompany()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_COMPANY);
        }

        Company company = companyMapper.toEntity(companyCreateRequestDTO);

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

    @Transactional
    public void updatePaymentDateTime(CompanyPaymentDatetimeUpdateDTO companyPaymentDatetimeUpdateDTO) {

        if(!isBetween1And28(companyPaymentDatetimeUpdateDTO.getDate())){
            throw new CustomException(ErrorCode.NOT_MATCHED_PAYMENT_DATE);
        }

        Company company = getCompany();

        company.updatePaymentDatetime(companyPaymentDatetimeUpdateDTO);

        companyCommandRepository.save(company);
    }

    @Transactional
    public void createCompanySeal(MultipartFile seal) {

        String sealUrl = fileService.uploadSeal(seal);

        Company company = getCompany();

        company.updateCompanySeal(sealUrl);

        companyCommandRepository.save(company);
    }

    public boolean isCreatedCompany() {
        return companyCommandRepository.existsById(COMPANY_ID);
    }

    public Company getCompany() {
        return companyCommandRepository.findById(COMPANY_ID).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_COMPANY)
        );
    }

    // 백엔드 작업시 사용 가능한 다음 월급날 조회 메소드 (시간 필요없을것 같아서 LocalDate)
    public LocalDate getNextPaymentDate() {
        Company company = getCompany();
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue() + 1;
        Integer day = company.getPaymentDatetime();
        if(month == 13){
            month = 1;
            year++;
        }
        return LocalDate.of(year, month, day);
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
