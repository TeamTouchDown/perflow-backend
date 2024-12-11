package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.service.HumanResourceCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/hrperfo")
@RequiredArgsConstructor
public class PerformanceHumanResourcePerfoCommandController {

    private final HumanResourceCommandService humanResourceCommandService;

    // 인사 평가 생성
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createHrPerfo(
            @PathVariable("empId") String empId) {

        humanResourceCommandService.createHumanResource(empId);

        return ResponseEntity.ok(SuccessCode.HRPERFO_UPLOAD_SUCCESS);
    }

    // 인사 평가 수정
    @PutMapping("/{empId}")
    public ResponseEntity<SuccessCode> updateHrPerfo(
            @PathVariable("empId") String empId,
            @RequestParam("score") Double score) {

        humanResourceCommandService.updateHumanResource(empId,score);

        return ResponseEntity.ok(SuccessCode.HRPERFO_UPDATE_SUCCESS);
    }
}
