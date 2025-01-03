package com.touchdown.perflowbackend.hr.command.domain.aggregate;

import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyAnnualCountUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyPaymentDatetimeUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyCreateRequestDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.company.CompanyUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "company", schema = "perflow")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "chairman", nullable = false, length = 30)
    private String chairman;

    @Column(name = "establish", nullable = false)
    private LocalDateTime establish;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "contact", nullable = false, length = 30)
    private String contact;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "seal", nullable = true)
    private String seal;

    @Column(name = "annual_count", nullable = false)
    private Integer annualCount;

    @Column(name = "payment_datetime", nullable = false)
    private Integer paymentDatetime;

    @Builder
    public Company(CompanyCreateRequestDTO requestDTO) {

        this.name = requestDTO.getName();
        this.chairman = requestDTO.getChairman();
        this.establish = requestDTO.getEstablish();
        this.address = requestDTO.getAddress();
        this.contact = requestDTO.getContact();
        this.email = requestDTO.getEmail();
        this.annualCount = requestDTO.getAnnualCount();
        this.paymentDatetime = requestDTO.getPaymentDatetime();
    }

    public void updateCompany(CompanyUpdateRequestDTO companyUpdateRequestDTO) {

        this.establish = companyUpdateRequestDTO.getEstablish();
        this.address = companyUpdateRequestDTO.getAddress();
        this.contact = companyUpdateRequestDTO.getContact();
        this.email = companyUpdateRequestDTO.getEmail();
    }

    public void updateAnnualCount (CompanyAnnualCountUpdateDTO companyAnnualCountUpdateDTO) {

        this.annualCount = companyAnnualCountUpdateDTO.getCompanyAnnualCount();
    }

    public void updatePaymentDatetime(CompanyPaymentDatetimeUpdateDTO companyPaymentDatetimeUpdateDTO) {

        this.paymentDatetime = companyPaymentDatetimeUpdateDTO.getDate();
    }

    public void updateCompanySeal(String sealUrl) {

        this.seal = sealUrl;
    }
}