package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.AiSummaryResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.AiSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/ai/summary")
@RequiredArgsConstructor
public class PerfomanceAiSummaryQueryController {

    private final AiSummaryService aiSummaryService;

    // Ai 요약 조회
    @GetMapping("/{empId}")
    public ResponseEntity<AiSummaryResponseDTO> getAiSummary(
            @PathVariable("empId") String empId,
            @RequestParam(name = "type") String type) {

        AiSummaryResponseDTO aiSummaryResponseDTO = aiSummaryService.getAiSummary(empId, type);

        return ResponseEntity.ok(aiSummaryResponseDTO);
    }
}
