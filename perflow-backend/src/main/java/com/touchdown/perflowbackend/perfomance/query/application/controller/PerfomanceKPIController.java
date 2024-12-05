package com.touchdown.perflowbackend.perfomance.query.application.controller;

import com.touchdown.perflowbackend.perfomance.query.application.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.application.service.PersonalKPIQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfomances/kpi")
@RequiredArgsConstructor
public class PerfomanceKPIController {

    private final PersonalKPIQueryService personalKPIQueryService;

    // 개인 KPI 리스트 조회
    @GetMapping("/personal/{userId}")
    public ResponseEntity<KPIListResponseDTO> getPersonalKPIs(
            @PathVariable(name = "userId") String userId
    ) {

        // 유저 사번 이용하여 개인 KPI 목록 및 제한 호출
        KPIListResponseDTO response = personalKPIQueryService.getPersonalKPIs(userId);

        return ResponseEntity.ok(response);
    }
}
