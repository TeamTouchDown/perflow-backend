package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RatioGradeResponseDTO {

    private final String EmpName;

    private final Long sRatio;

    private final Long aRatio;

    private final Long bRatio;

    private final Long cRatio;

    private final Long dRatio;

    private final String reason;
}
