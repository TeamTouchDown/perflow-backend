package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/personal")
@RequiredArgsConstructor
public class PerformanceKPIPersonalCommandController {

    private final KPICommandService kpiCommandService;

    // 개인 KPI 입력 받아 저장
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createPersonalKPI(
            @PathVariable("empId") String empId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {

        kpiCommandService.createPersonalKpi(kpiDetailRequestDTO, empId);

        return ResponseEntity.ok(SuccessCode.KPI_PERSONAL_UPLOAD_SUCCESS);
    }

    // 개인 KPI 입력 받아 수정
    @PutMapping("/{kpiId}")
    public ResponseEntity<SuccessCode> updatePersonalKPI(
            @PathVariable("kpiId") Long kpiId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {
        kpiCommandService.updatePersonalKpi(kpiDetailRequestDTO, kpiId);

        return ResponseEntity.ok(SuccessCode.KPI_PERSONAL_UPDATE_SUCCESS);
    }

    // 개인 KPI 입력 받아 삭제
    @DeleteMapping("/{kpiId}/{empId}")
    public ResponseEntity<SuccessCode> deletePersonalKPI(
            @PathVariable("kpiId") Long kpiId,
            @PathVariable("empId") String empId ) {

        kpiCommandService.deletePersonalKpi(kpiId, empId);

        return ResponseEntity.ok(SuccessCode.KPI_PERSONAL_DELETE_SUCCESS);
    }
}
