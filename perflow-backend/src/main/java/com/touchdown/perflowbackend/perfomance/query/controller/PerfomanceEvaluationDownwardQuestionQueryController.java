package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.query.service.EvaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/perfomances/downward/perfo/question")
@RequiredArgsConstructor
public class PerfomanceEvaluationDownwardQuestionQueryController {

    private final EvaQueryService evaQueryService;

    // 동료 평가 문항 리스트 조회
    @GetMapping("/{empId}")
    public ResponseEntity<List<EvaQuestionDetailResponseDTO>> getEvaColQuestionList(
            @PathVariable(name = "empId") String empId,
            @RequestParam(name = "deptId") Long deptId,
            @RequestParam(name = "questionType") String questionType,
            @RequestParam(name = "perfoType") String perfoType){

        EvaQuestionRequestDTO evaQuestionRequestDTO = EvaQuestionRequestDTO.builder()
                .deptId(deptId)
                .questionType(questionType)
                .perfoType(perfoType)
                .build();


        List<EvaQuestionDetailResponseDTO> response = evaQueryService.getEvaQuestionList(empId, evaQuestionRequestDTO);

        return ResponseEntity.ok(response);
    }
}
