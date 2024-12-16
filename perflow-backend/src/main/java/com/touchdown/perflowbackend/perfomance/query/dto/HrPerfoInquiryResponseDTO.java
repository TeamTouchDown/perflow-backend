package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HrPerfoInquiryResponseDTO {

    // 사진 파트 추가 예정

    private final String empId;

    private final String empName;

    private final String empDeptName;

    private final String empPositionName;

    private final String empJobName;

    private final String hrPerfoInquiryReason;

    private final Long hrPerfoColScore;

    private final Long hrPerfoDownScore;

    private final String hrPerfoEmpName;
}
