package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.CompanyCreateRequestDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import com.touchdown.perflowbackend.hr.query.dto.CompanyResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyCreateRequestDTO companyCreateRequestDTO) {

        return Company.builder()
                .requestDTO(companyCreateRequestDTO)
                .build();
    }

    public CompanyResponseDTO toResponse(Company company) {

        return CompanyResponseDTO.builder()
                .company(company)
                .build();
    }
}
