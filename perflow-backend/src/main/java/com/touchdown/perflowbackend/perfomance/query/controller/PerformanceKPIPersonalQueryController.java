package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.KPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/personal")
@RequiredArgsConstructor
public class PerformanceKPIPersonalQueryController {

    private final KPIQueryService KPIQueryService;

    // 개인 KPI 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<KPIListResponseDTO> getPersonalKPIs(
            @PathVariable(name = "empId") String empId
    ) {

        // 유저 사번 이용하여 개인 KPI 목록 및 제한 호출
        KPIListResponseDTO response = KPIQueryService.getPersonalKPIs(empId);

        return ResponseEntity.ok(response);
    }
}
