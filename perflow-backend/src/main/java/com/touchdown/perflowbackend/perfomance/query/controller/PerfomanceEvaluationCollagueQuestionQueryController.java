package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.query.service.EvaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/perfomances/col/perfo/question")
@RequiredArgsConstructor
public class PerfomanceEvaluationCollagueQuestionQueryController {

    private final EvaQueryService evaQueryService;

    // 동료 평가 문항 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<List<EvaQuestionDetailResponseDTO>> getEvaColQuestionList(
            @PathVariable(name = "empId") String empId,
            @RequestBody EvaQuestionRequestDTO evaQuestionRequestDTO){

        List<EvaQuestionDetailResponseDTO> response = evaQueryService.getEvaQuestionList(empId, evaQuestionRequestDTO);

        return ResponseEntity.ok(response);
    }
}
