package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttributeOvertimeCommandService;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttribute-OverTime-Controller", description = "초과근무 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttributeOvertimeCommandController {

    private final WorkAttributeOvertimeCommandService workAttributeOvertimeCommandService;
    // 직원: 초과근무 등록
    @Operation(summary = "직원의 초과근무 등록", description = "직원이 새로운 초과근무 요청을 등록합니다.")
    @PostMapping("/emp/overtimes")
    public ResponseEntity<SuccessCode> createOvertime(@Valid @RequestBody WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        workAttributeOvertimeCommandService.createOvertime(workAttributeOvertimeForEmployeeRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_SUCCESS);
    }

    // 직원: 초과근무 삭제
    @Operation(summary = "직원의 초과근무 삭제", description = "직원이 본인의 초과근무 요청을 삭제합니다.")
    @DeleteMapping("/emp/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteOvertime(@PathVariable Long overtimeId) {
        workAttributeOvertimeCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS);
    }

    // 직원: 소급 신청
    @Operation(summary = "직원의 초과근무 소급 신청", description = "직원이 초과근무 요청에 대한 소급 처리를 신청합니다.")
    @PostMapping("/emp/overtimes/{overtimeId}/retroactive")
    public ResponseEntity<SuccessCode> applyForRetroactiveOvertime(@PathVariable Long overtimeId,
                                                                   @RequestBody String reason) {
        workAttributeOvertimeCommandService.applyForRetroactiveOvertime(overtimeId, reason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_RETROACTIVE_SUCCESS);
    }

    // 팀장: 소급 승인/반려
    @Operation(summary = "팀장의 초과근무 소급 승인/반려", description = "팀장이 직원의 초과근무 소급 요청을 승인 또는 반려합니다.")
    @PutMapping("/leader/overtimes/{overtimeId}/retroactive")
    public ResponseEntity<SuccessCode> approveOrRejectRetroactiveOvertime(@PathVariable Long overtimeId,
                                                                          @RequestParam String decision,
                                                                          @RequestBody String reason) {
        workAttributeOvertimeCommandService.approveOrRejectRetroactiveOvertime(overtimeId, decision, reason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_RETROACTIVE_DECISION_SUCCESS);
    }

    // 팀장: 팀원 초과근무 상태 변경 (승인/반려)
    @Operation(summary = "팀장의 초과근무 상태 변경", description = "팀장이 직원의 초과근무 요청 상태를 승인 또는 반려로 변경합니다.")
    @PutMapping("/leader/overtimes/status/{overtimeId}")
    public ResponseEntity<SuccessCode> updateOvertimeStatus(@PathVariable Long overtimeId,
                                                            @RequestBody WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        workAttributeOvertimeCommandService.updateOvertimeStatus(overtimeId, workAttributeOvertimeForEmployeeRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS);
    }

    // 팀장: 팀원 초과근무 삭제
    @Operation(summary = "팀장의 초과근무 요청 삭제", description = "팀장이 직원의 초과근무 요청을 삭제합니다.")
    @DeleteMapping("/leader/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteTeamOvertime(@PathVariable Long overtimeId) {
        workAttributeOvertimeCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS);
    }
}
