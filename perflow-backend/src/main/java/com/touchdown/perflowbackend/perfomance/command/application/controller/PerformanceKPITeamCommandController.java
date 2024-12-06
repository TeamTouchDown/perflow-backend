package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leader/perfomances/kpi/team")
@RequiredArgsConstructor
public class PerformanceKPITeamCommandController {

    private final KPICommandService kpiCommandService;

    // 팀 KPI 입력 받아 추가
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createTeamKPI(
            @PathVariable("empId") String empId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {

        kpiCommandService.createTeamKpi(kpiDetailRequestDTO, empId);

        return ResponseEntity.ok(SuccessCode.KPI_TEAM_UPLOAD_SUCCESS);
    }

    // 팀 KPI 입력 받아 수정
    @PutMapping("/{kpiId}")
    public ResponseEntity<SuccessCode> updateTeamKPI(
            @PathVariable("kpiId") Long kpiId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {
        kpiCommandService.updateTeamKpi(kpiDetailRequestDTO, kpiId);

        return ResponseEntity.ok(SuccessCode.KPI_TEAM_UPDATE_SUCCESS);
    }

    // 팀 KPI 입력 받아 삭제
    @DeleteMapping("/{kpiId}/{empId}")
    public ResponseEntity<SuccessCode> deleteTeamKPI(
            @PathVariable("kpiId") Long kpiId,
            @PathVariable("empId") String empId ) {

        kpiCommandService.deleteTeamKpi(kpiId, empId);

        return ResponseEntity.ok(SuccessCode.KPI_TEAM_DELETE_SUCCESS);
    }
}
