package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.CompanyRegisterRequestDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyCommandMapper {

    public Company toEntity(CompanyRegisterRequestDTO companyRegisterRequestDTO) {

        return Company.builder()
                .requestDTO(companyRegisterRequestDTO)
                .build();
    }
}
