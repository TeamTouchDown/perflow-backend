package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAnnualCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-Annual-Controller", description = "연차 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeAnnualCommandController {

    private final WorkAttitudeAnnualCommandService annualCommandService;

    // 사원 연차 신청
    @Operation(summary = "연차 신청", description = "사원이 연차를 신청합니다. 전체 연차 개수를 초과하지 않도록 검증합니다.")
    @PostMapping("/emp/annual")
    public ResponseEntity<String> registerAnnual(@RequestBody WorkAttitudeAnnualRequestDTO requestDTO) {
        annualCommandService.registerAnnual(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_SUCCESS.getMessage());
    }

    // 사원 연차 수정
    @Operation(summary = "연차 수정", description = "사원이 본인의 연차 신청을 수정합니다.")
    @PutMapping("/emp/annual/{annualId}")
    public ResponseEntity<String> updateAnnual(@PathVariable Long annualId,
                                               @RequestBody WorkAttitudeAnnualRequestDTO requestDTO) {
        annualCommandService.updateAnnual(annualId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }

    // 사원 연차 삭제
    @Operation(summary = "연차 삭제", description = "사원이 본인의 연차 신청을 삭제합니다.")
    @DeleteMapping("/emp/annual/{annualId}")
    public ResponseEntity<String> deleteAnnual(@PathVariable Long annualId) {
        annualCommandService.softDeleteAnnual(annualId);
        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }

    // 팀장 연차 승인
    @Operation(summary = "연차 승인", description = "팀장이 사원의 연차 신청을 승인합니다.")
    @PutMapping("/leader/annual/{annualId}/approve")
    public ResponseEntity<String> approveAnnual(@PathVariable Long annualId) {
        annualCommandService.approveAnnual(annualId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_CHECK_IN_SUCCESS.getMessage());
    }

    // 팀장 연차 반려
    @Operation(summary = "연차 반려", description = "팀장이 사원의 연차 신청을 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/annual/{annualId}/reject")
    public ResponseEntity<String> rejectAnnual(@PathVariable Long annualId, @RequestBody String rejectReason) {
        annualCommandService.rejectAnnual(annualId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_CHECK_OUT_SUCCESS.getMessage());
    }
}
