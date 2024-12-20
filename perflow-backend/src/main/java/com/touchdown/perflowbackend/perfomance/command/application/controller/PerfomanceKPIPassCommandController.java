package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreateKpiPassDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/kpi/pass")
@RequiredArgsConstructor
public class PerfomanceKPIPassCommandController {

    private final KPICommandService kpiCommandService;

    @PostMapping("/{empId}/{kpiId}")
    public ResponseEntity<SuccessCode> createKpiPass(
            @PathVariable("empId") String empId,
            @PathVariable("kpiId") Long kpiId,
            @RequestBody CreateKpiPassDTO createKpiPassDTO) {

        kpiCommandService.createKpiPass(empId, kpiId, createKpiPassDTO);

        return ResponseEntity.ok(SuccessCode.KPI_PASS_UPLOAD_SUCCESS);
    }
}
