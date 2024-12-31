package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.EvaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leader/perfomances/downward/perfo")
@RequiredArgsConstructor
public class PerformanceEvaluationDownwardQueryController {

    private final EvaQueryService evaQueryService;

    // 하향 평가 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<List<EvaDetailResponseDTO>> getEvaDownList(
            @PathVariable(name = "empId") String empId) {

        List<EvaDetailResponseDTO> response = evaQueryService.getEvaDownList(empId);

        return ResponseEntity.ok(response);
    }


    // 하향 평가 상세 조회
    @GetMapping("/{perfoempId}/{perfoedempId}")
    public ResponseEntity<List<EvaAnswerResponseDTO>> getEvaDownDetail(
            @PathVariable(name = "perfoempId") String perfoempId,
            @PathVariable(name = "perfoedempId") String perfoedempId ){

        List<EvaAnswerResponseDTO> response = evaQueryService.getEvaDownAnswer(perfoempId, perfoedempId);

        return ResponseEntity.ok(response);
    }
}
