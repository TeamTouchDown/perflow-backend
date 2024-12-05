package com.touchdown.perflowbackend.perfomance.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KPILimitResponseDTO {

    private final Long minKpis;
    private final Long maxKpis;
}
