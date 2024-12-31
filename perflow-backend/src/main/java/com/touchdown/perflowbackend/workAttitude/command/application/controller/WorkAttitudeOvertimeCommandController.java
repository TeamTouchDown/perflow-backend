package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeOvertimeCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-OverTime-Controller", description = "초과근무 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeOvertimeCommandController {

    private final WorkAttitudeOvertimeCommandService overtimeCommandService;

    // 사원 초과근무 신청
    @Operation(summary = "초과근무 신청", description = "사원이 초과근무를 신청합니다.")
    @PostMapping("/emp/overtimes")
    public ResponseEntity<SuccessCode> createOvertime(@RequestBody WorkAttitudeOvertimeRequestDTO requestDTO) {
        overtimeCommandService.createOvertime(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_SUCCESS);
    }

    // 사원 초과근무 수정
    @Operation(summary = "초과근무 수정", description = "사원이 본인의 초과근무 신청을 수정합니다.")
    @PutMapping("/emp/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> updateOvertime(@PathVariable Long overtimeId,
                                                      @RequestBody WorkAttitudeOvertimeRequestDTO requestDTO) {
        overtimeCommandService.updateOvertime(overtimeId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS);
    }

    // 사원 초과근무 삭제
    @Operation(summary = "초과근무 삭제", description = "사원이 본인의 초과근무 신청을 삭제합니다.")
    @DeleteMapping("/emp/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteOvertime(@PathVariable Long overtimeId) {
        overtimeCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS);
    }

    // 팀장 초과근무 승인
    @Operation(summary = "초과근무 승인", description = "팀장이 초과근무를 승인합니다.")
    @PutMapping("/leader/overtimes/{overtimeId}/approve")
    public ResponseEntity<SuccessCode> approveOvertime(@PathVariable Long overtimeId) {
        overtimeCommandService.approveOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_APPROVED);
    }

    // 팀장 초과근무 반려
    @Operation(summary = "초과근무 반려", description = "팀장이 초과근무를 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/overtimes/{overtimeId}/reject")
    public ResponseEntity<SuccessCode> rejectOvertime(@PathVariable Long overtimeId,
                                                      @RequestBody String rejectReason) {
        overtimeCommandService.rejectOvertime(overtimeId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_REJECTED);
    }

}
