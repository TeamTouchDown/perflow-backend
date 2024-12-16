package com.touchdown.perflowbackend.perfomance.query.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RatioPerfoResponseDTO {

    private final String deptName;

    private final Long personalKpiWeight;

    private final Long teamKpiWeight;

    private final Long colWeight;

    private final Long downWeight;

    private final Long attendanceWeight;

    private final String reason;
}
