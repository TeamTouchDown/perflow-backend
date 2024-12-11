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
@RequestMapping("/api/v1/perfomances/col/perfo")
@RequiredArgsConstructor
public class PerformanceEvaluationCollagueQueryController {

    private final EvaQueryService evaQueryService;

    // 동료 평가 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<List<EvaDetailResponseDTO>> getEvaColList(
            @PathVariable(name = "empId") String empId) {

        List<EvaDetailResponseDTO> response = evaQueryService.getEvaColList(empId);

        return ResponseEntity.ok(response);
    }


    // 동료 평가 상세 조회
    @GetMapping("/{perfoempId}/{perfoedempId}")
    public ResponseEntity<List<EvaAnswerResponseDTO>> getEvaColDetail(
            @PathVariable(name = "perfoempId") String perfoempId,
            @PathVariable(name = "perfoedempId") String perfoedempId ){

        List<EvaAnswerResponseDTO> response = evaQueryService.getEvaColAnswer(perfoempId, perfoedempId);

        return ResponseEntity.ok(response);
    }
}
