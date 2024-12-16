package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Weight;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioPerfoResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.RatioQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hr/perfomances/ratio")
@RequiredArgsConstructor
public class PerfomanceRatioQueryCommand {

    private final RatioQueryService ratioQueryService;

    // 인사 평가 가중치 조회
    @GetMapping("/perfo/{deptId}")
    public ResponseEntity<RatioPerfoResponseDTO> getPerfoWeight(
            @PathVariable("deptId") Long deptId) {

        RatioPerfoResponseDTO response = ratioQueryService.getPerfoWeight(deptId);

        return ResponseEntity.ok(response);
    }
}
