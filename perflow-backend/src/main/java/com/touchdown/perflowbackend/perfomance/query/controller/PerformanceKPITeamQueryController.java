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
@RequestMapping("/api/v1/leader/perfomances/kpi/team")
@RequiredArgsConstructor
public class PerformanceKPITeamQueryController {

    private final KPIQueryService KPIQueryService;

    // 팀 KPI 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<KPIListResponseDTO> getTeamKPIs(
            @PathVariable(name = "empId") String empId
    ) {

        // 유저 사번 이용하여 팀 KPI 목록 및 제한 호출
        KPIListResponseDTO response = KPIQueryService.getTeamKPIs(empId);

        return ResponseEntity.ok(response);
    }
}
