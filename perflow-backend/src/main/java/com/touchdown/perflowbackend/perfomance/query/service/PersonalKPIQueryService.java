package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.KPIQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper.kpiListToDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalKPIQueryService {

    private final KPIQueryRepository kpiQueryRepository;

    // 개인 KPI 리스트 조회
    @Transactional(readOnly = true)
    public KPIListResponseDTO getPersonalKPIs(String empId) {

        // 해당 유저의 개인 KPI 가져오기
        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findPersonalKPIsByuserId(empId);

        // 해당 유저의 부서의 KPI 제한치 가져오기
        KPILimitResponseDTO limit = kpiQueryRepository.findPersonalKPILimitByUserId(empId)
                .orElse(new KPILimitResponseDTO(2L, 5L)); // 기본값 설정


        // 가져온 개인 KPI를 리스트에 넣어 처리하기
        return kpiListToDTO(lists,limit);
    }
}
