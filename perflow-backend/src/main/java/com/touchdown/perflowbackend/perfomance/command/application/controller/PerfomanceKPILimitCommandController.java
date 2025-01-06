package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/limit")
@RequiredArgsConstructor
public class PerfomanceKPILimitCommandController {

    private final KPICommandService kpiCommandService;

    @PostMapping("/{deptId}/{empId}")
    public ResponseEntity<SuccessCode> createKPILimit(
            @PathVariable Long deptId,
            @PathVariable String empId,
            @RequestBody KPILimitDTO limitDTO) {

        kpiCommandService.createKPILimit(deptId,empId,limitDTO);

        return ResponseEntity.ok(SuccessCode.KPI_LIMIT_UPLOAD_SUCCESS);
    }
}
