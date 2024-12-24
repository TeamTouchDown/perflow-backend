package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.KPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/team")
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

    // 팀 KPI 리스트 년도별 조회(현재)
    @GetMapping("/period/current/{empId}/{year}")
    public ResponseEntity<KPIListResponseDTO> getCurrentTeamKPIsByYear(
            @PathVariable(name = "empId") String empId,
            @PathVariable String year,
            @RequestParam(required = false) String quarter,
            @RequestParam(required = false) String month
    ) {

        // 유저 사번 이용하여 개인 KPI 목록 및 제한 호출
        KPIListResponseDTO response = KPIQueryService.getTeamCurrentKPIsByYear(empId, year, quarter, month);

        return ResponseEntity.ok(response);
    }

    // 팀 KPI 리스트 년도별 조회(과거)
    @GetMapping("/period/past/{empId}/{year}")
    public ResponseEntity<KPIListResponseDTO> getPastTeamKPIsByYear(
            @PathVariable(name = "empId") String empId,
            @PathVariable String year,
            @RequestParam(required = false) String quarter,
            @RequestParam(required = false) String month
    ) {

        // 유저 사번 이용하여 개인 KPI 목록 및 제한 호출
        KPIListResponseDTO response = KPIQueryService.getTeamPastKPIsByYear(empId, year, quarter, month);

        return ResponseEntity.ok(response);
    }
}
