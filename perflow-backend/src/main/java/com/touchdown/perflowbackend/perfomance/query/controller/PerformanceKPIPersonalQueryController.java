package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.KPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    // 개인 KPI 리스트 기간별 조회 (생성)
    @GetMapping("/period/new/{empId}/{year}")
    public ResponseEntity<KPIListResponseDTO> getNewPersonalKPIs(
            @PathVariable(name = "empId") String empId,
            @PathVariable(name = "year") String year,
            @RequestParam(required = false) String quarter,
            @RequestParam(required = false) String month
    ) {
        KPIListResponseDTO response;

        if (Objects.equals(month, "")) {
            month = null;
        }
        if (Objects.equals(quarter, "")) {
            quarter = null;
        }

        // 조건: quarter와 month가 모두 null인 경우
        if (quarter == null && month == null) {
            // 년도별 조회 함수 호출
            response = KPIQueryService.getNewPersonalKPIsByempId(empId, year);
        } else {
            // 기간별 조회 함수 호출
            response = KPIQueryService.getNewPersonalKPIsempId(empId, year, quarter, month);
        }
        return ResponseEntity.ok(response);
    }

    // 개인 KPI 리스트 기간별 조회 (현재)
    @GetMapping("/period/current/{empId}/{year}")
    public ResponseEntity<KPIListResponseDTO> getCurrentPersonalKPIsByYear(
            @PathVariable(name = "empId") String empId,
            @PathVariable String year,
            @RequestParam(required = false) String quarter,
            @RequestParam(required = false) String month

    ) {
        KPIListResponseDTO response;

        if (Objects.equals(month, "")) {
            month = null;
        }
        if (Objects.equals(quarter, "")) {
            quarter = null;
        }

        // 조건: quarter와 month가 모두 null인 경우
        if (quarter == null && month == null) {
            // 년도별 조회 함수 호출
            response = KPIQueryService.getCurrentPersonalKPIsByOnlyYear(empId, year);
        } else {
            // 기간별 조회 함수 호출
            response = KPIQueryService.getCurrentPersonalKPIsByYear(empId, year, quarter, month);
        }
        return ResponseEntity.ok(response);
    }

    // 개인 KPI 리스트 기간별 조회 (과거)
    @GetMapping("/period/past/{empId}/{year}")
    public ResponseEntity<KPIListResponseDTO> getPastPersonalKPIsByYear(
            @PathVariable(name = "empId") String empId,
            @PathVariable String year,
            @RequestParam(required = false) String quarter,
            @RequestParam(required = false) String month

    ) {

        KPIListResponseDTO response;

        if (Objects.equals(month, "")) {
            month = null;
        }
        if (Objects.equals(quarter, "")) {
            quarter = null;
        }

        // 조건: quarter와 month가 모두 null인 경우
        if (quarter == null && month == null) {
            // 년도별 조회 함수 호출
            response = KPIQueryService.getPastPersonalKPIsByOnlyYear(empId, year);
        } else {
            // 기간별 조회 함수 호출
            response = KPIQueryService.getPastPersonalKPIsByYear(empId, year, quarter, month);
        }
        // 유저 사번 이용하여 개인 KPI 목록 및 제한 호출
        return ResponseEntity.ok(response);
    }
}
