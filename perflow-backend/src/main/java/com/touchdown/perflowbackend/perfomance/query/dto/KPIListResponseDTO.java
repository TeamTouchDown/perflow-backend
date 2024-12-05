package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class KPIListResponseDTO {

    private List<KPIDetailResponseDTO> kpiLists;
    private Long totalKpis;
    private Long minKpis;
    private Long maxKpis;
}
