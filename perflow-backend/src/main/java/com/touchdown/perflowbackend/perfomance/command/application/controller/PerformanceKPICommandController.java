package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/kpi")
@RequiredArgsConstructor
public class PerformanceKPICommandController {

    private final KPICommandService kpiCommandService;

    // 개인 KPI 입력 받아 저장
    @PostMapping("/personal/{empId}")
    public ResponseEntity<SuccessCode> createPersonalKPI(
            @PathVariable("empId") String empId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {

        kpiCommandService.createPersonalKpi(kpiDetailRequestDTO, empId);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 개인 KPI 입력 받아 수정
    @PutMapping("/personal/{kpiId}")
    public ResponseEntity<SuccessCode> updatePersonalKPI(
            @PathVariable("kpiId") Long kpiId,
            @RequestBody KPIDetailRequestDTO kpiDetailRequestDTO) {
        kpiCommandService.updatePersonalKpi(kpiDetailRequestDTO, kpiId);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 개인 KPI 입력 받아 삭제
    @DeleteMapping("/personal/{kpiId}")
    public ResponseEntity<SuccessCode> deletePersonalKPI(
            @PathVariable("kpiId") Long kpiId,
            @RequestBody String empId ) {

        kpiCommandService.deletePersonalKpi(kpiId, empId);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

}
