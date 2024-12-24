package com.touchdown.perflowbackend.hr.command.application.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
// 급여지급일, 연차 지급 개수 수정은 다른 api로 분리 예정
public class CompanyUpdateRequestDTO {

    private LocalDateTime establish; // 설립일

    private String address;

    private String contact;

    private String email;
}
