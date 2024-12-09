package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EvaDetailResponseDTO {

    // 사진 추가 작업 거쳐야함

    private final String empId;
    private final String empName;
    private final String empDeptName;
    private final String empPositionName;
    private final String empJobName;
    private final Boolean existence;

}
