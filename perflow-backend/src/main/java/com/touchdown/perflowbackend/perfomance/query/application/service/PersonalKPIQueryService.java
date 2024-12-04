package com.touchdown.perflowbackend.perfomance.query.application.service;

import com.touchdown.perflowbackend.perfomance.query.application.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.application.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.domain.repository.KPIQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalKPIQueryService {

    private final KPIQueryRepository kpiQueryRepository;

//    @Transactional(readOnly = true)
//    public KPIListResponseDTO getKPIs(Long userId) {

//        // 해당 유저의 개인 KPI 가져오기
//        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findKPIsByKpiId(userId);
//
//        // 가져온 개인 KPI를 리스트에 넣어 처리하기
//        return KPIListResponseDTO.builder()
//                .kpiLists(lists)    // 개인 kpi 목록
//                .totalKpis(lists.size())
//                .
//
//
//    }

}
