package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.KPIRejectReponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.KPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfomances/kpi/reject")
@RequiredArgsConstructor
public class PerfomanceKPIPassQueryController {

    private final KPIQueryService kpiQueryService;

    @GetMapping("/{empId}/{kpiId}")
    public ResponseEntity<KPIRejectReponseDTO> getKPIRejectResponse(
            @PathVariable("empId") String empId,
            @PathVariable("kpiId") Long kpiId) {

        KPIRejectReponseDTO response = kpiQueryService.getKPIRejectResponse(empId,kpiId);

        return ResponseEntity.ok(response);
    }
}
