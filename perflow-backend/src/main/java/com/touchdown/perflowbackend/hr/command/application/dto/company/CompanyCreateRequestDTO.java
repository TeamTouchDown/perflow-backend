package com.touchdown.perflowbackend.hr.command.application.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CompanyCreateRequestDTO {

    private String name;

    private String chairman;

    private LocalDateTime establish; // 설립일

    private String address;

    private String contact;

    private String email;

    private Integer annualCount; // 연자 지급 개수

    private Integer paymentDatetime; // 급여 지급일
}
