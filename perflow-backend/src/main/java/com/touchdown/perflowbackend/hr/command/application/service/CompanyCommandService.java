package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.dto.CompanyRegisterRequestDTO;
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

    @Transactional
    public void registerCompany(CompanyRegisterRequestDTO companyRegisterRequestDTO) {

        // 이미 회사 데이터가 존재하는지 확인
        if (companyCommandRepository.existsById(1L)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_COMPANY);
        }

        Company company = companyCommandMapper.toEntity(companyRegisterRequestDTO);

        companyCommandRepository.save(company);
    }
}
