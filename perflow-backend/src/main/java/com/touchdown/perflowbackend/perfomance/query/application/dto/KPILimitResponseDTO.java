package com.touchdown.perflowbackend.perfomance.query.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KPILimitResponseDTO {

    private final Long minKpis;
    private final Long maxKpis;

    public KPILimitResponseDTO(Long minKpis, Long maxKpis){

        this.minKpis = minKpis;
        this.maxKpis = maxKpis;
    }
}
