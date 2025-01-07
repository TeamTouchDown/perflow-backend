package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIRejectReponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.KPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/limit")
@RequiredArgsConstructor
public class PerfomanceKPILimitQueryController {

    private final KPIQueryService kpiQueryService;

    @GetMapping("/{deptId}")
    public ResponseEntity<KPILimitDTO> getKPILimit(
            @PathVariable("deptId") Long deptId) {

        KPILimitDTO response = kpiQueryService.getKPILimit(deptId);

        return ResponseEntity.ok(response);
    }
}
