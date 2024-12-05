package com.touchdown.perflowbackend.perfomance.command.mapper;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;

import java.util.List;

public class PerformanceMapper {

    // 개인 kpi 리스트 및 제한 DTO 빌더
    public static KPIListResponseDTO kpiListToDTO(List<KPIDetailResponseDTO> lists, KPILimitResponseDTO limit){

        return KPIListResponseDTO.builder()
                .kpiLists(lists)    // 개인 kpi 목록
                .totalKpis((long) lists.size())    // 현재 kpi 갯수
                .minKpis(limit.getMinKpis())          // kpi 최소 제한 갯수
                .maxKpis(limit.getMaxKpis())          // kpi 최대 제한 갯수
                .build();
    }
}
