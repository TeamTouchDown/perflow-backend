package com.touchdown.perflowbackend.hr.query.dto;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CompanyResponseDTO {

    private String name;

    private String chairman;

    private LocalDateTime establish; // 설립일

    private String address;

    private String contact;

    private String email;

    private Integer annualCount; // 연자 지급 개수

    private Integer paymentDatetime; // 급여 지급일

    @Builder
    public CompanyResponseDTO(Company company) {

        this.name = company.getName();
        this.chairman = company.getChairman();
        this.establish = company.getEstablish();
        this.address = company.getAddress();
        this.contact = company.getContact();
        this.email = company.getEmail();
        this.annualCount = company.getAnnualCount();
        this.paymentDatetime = company.getPaymentDatetime();
    }
}
