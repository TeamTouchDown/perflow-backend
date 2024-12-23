package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeVacationRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeVacationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-Vacation-Controller", description = "휴가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeVacationCommandController {

    private final WorkAttitudeVacationCommandService vacationCommandService;

    // 사원 휴가 관련 API
    @Operation(summary = "휴가 신청", description = "사원이 휴가를 신청합니다.")
    @PostMapping("/emp/vacation")
    public ResponseEntity<String> registerVacation(@RequestBody WorkAttitudeVacationRequestDTO requestDTO) {
        vacationCommandService.registerVacation(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_TRAVEL_SUCCESS.getMessage());
    }

    @Operation(summary = "휴가 수정", description = "사원이 휴가를 수정합니다.")
    @PutMapping("/emp/vacation/{vacationId}")
    public ResponseEntity<String> updateVacation(@PathVariable Long vacationId,
                                                 @RequestBody WorkAttitudeVacationRequestDTO requestDTO) {
        vacationCommandService.updateVacation(vacationId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS.getMessage());
    }

    @Operation(summary = "휴가 삭제", description = "사원이 휴가를 삭제합니다.")
    @DeleteMapping("/emp/vacation/{vacationId}")
    public ResponseEntity<String> deleteVacation(@PathVariable Long vacationId) {
        vacationCommandService.deleteVacation(vacationId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS.getMessage());
    }

    // 팀장 휴가 관련 API
    @Operation(summary = "휴가 승인", description = "팀장이 휴가를 승인합니다.")
    @PutMapping("/leader/vacation/{vacationId}/approve")
    public ResponseEntity<String> approveVacation(@PathVariable Long vacationId) {
        vacationCommandService.approveVacation(vacationId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_IN_SUCCESS.getMessage());
    }

    @Operation(summary = "휴가 반려", description = "팀장이 휴가를 반려합니다.")
    @PutMapping("/leader/vacation/{vacationId}/reject")
    public ResponseEntity<String> rejectVacation(@PathVariable Long vacationId, @RequestBody String rejectReason) {
        vacationCommandService.rejectVacation(vacationId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_OUT_SUCCESS.getMessage());
    }

    @Operation(summary = "휴가 반려 사유 작성", description = "팀장이 휴가 반려 사유를 작성합니다.")
    @PostMapping("/leader/vacation/{vacationId}/reject-reason")
    public ResponseEntity<String> writeRejectReason(@PathVariable Long vacationId, @RequestBody String rejectReason) {
        vacationCommandService.rejectVacation(vacationId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_OUT_SUCCESS.getMessage());
    }
}
