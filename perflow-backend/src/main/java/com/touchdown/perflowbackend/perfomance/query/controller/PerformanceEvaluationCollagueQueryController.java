package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.EvaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 동료 평가 점수 조회
    @GetMapping("/score/{empId}")
    public ResponseEntity<Long> getEvaColScore(
            @PathVariable(name = "empId") String empId){

        Long response = evaQueryService.getEvaColScore(empId);

        return ResponseEntity.ok(response);
    }

    // 동료 평가 답변 조회
    @GetMapping("/answer/{empId}")
    public ResponseEntity<List<String>> getEvaColAnswers(
            @PathVariable(name = "empId") String empId,
            @RequestParam(name = "questionId") Long questionId,
            @RequestParam(name = "deptId") Long deptId){

        List<String> response = evaQueryService.getEvaColAnswers(empId, questionId, deptId);

        return ResponseEntity.ok(response);
    }
}
