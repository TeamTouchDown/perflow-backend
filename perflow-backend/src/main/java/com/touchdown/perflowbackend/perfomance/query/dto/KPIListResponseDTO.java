package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class KPIListResponseDTO {

    private final List<KPIDetailResponseDTO> kpiLists;

    private final Long totalKpis;

    private final Long minKpis;

    private final Long maxKpis;
}
