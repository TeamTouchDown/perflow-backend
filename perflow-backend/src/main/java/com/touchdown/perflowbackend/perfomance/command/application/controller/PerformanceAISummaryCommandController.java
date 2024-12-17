package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.service.AISummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfomances/ai/summary")
@RequiredArgsConstructor
public class PerformanceAISummaryCommandController {

    private final AISummaryService aisummaryService;

    // AI Summary 생성
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createAISummary(
            @PathVariable("empId") String empId ) {

        aisummaryService.createAISummary(empId);

        return ResponseEntity.ok(SuccessCode.AI_SUMMARY_UPLOAD_SUCCESS);
    }
}
