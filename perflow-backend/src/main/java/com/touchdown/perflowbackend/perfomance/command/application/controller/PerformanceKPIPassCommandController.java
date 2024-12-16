package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreateKpiPassDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/kpi")
@RequiredArgsConstructor
public class PerformanceKPIPassCommandController {

    private final KPICommandService kpiCommandService;

    // KPI 최신화 생성
    @PostMapping("/pass/{empId}/{kpiId}")
    public ResponseEntity<SuccessCode> createKpiProgress(
            @PathVariable("empId") String empId,
            @PathVariable("kpiId") Long kpiId,
            @RequestBody CreateKpiPassDTO createKpiPassDTO) {

        kpiCommandService.createKpiProgress(empId, kpiId, createKpiPassDTO);

        return ResponseEntity.ok(SuccessCode.KPI_PROGRESS_UPLOAD_SUCCESS);
    }
}
