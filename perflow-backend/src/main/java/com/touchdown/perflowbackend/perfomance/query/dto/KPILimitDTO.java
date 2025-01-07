package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KPILimitDTO {

    private final Long personalminKpis;

    private final Long personalmaxKpis;

    private final Long teamminKpis;

    private final Long teammaxKpis;
}


