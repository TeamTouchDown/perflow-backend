package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.*;
import com.touchdown.perflowbackend.workAttitude.command.application.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-ApprovalRequest-Controller", description = "근태 관련 API 모음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeApprovalRequestCommandController {

    private final WorkAttitudeApprovalRequestCommandService approvalRequestCommandService;

/*    // -------------------------- 연차 API --------------------------

    // 사원 연차 신청
    @Operation(summary = "연차 신청", description = "사원이 연차를 신청합니다. 전체 연차 개수를 초과하지 않도록 검증합니다.")
    @PostMapping("/emp/approval/annual")
    public ResponseEntity<String> registerAnnual(@RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.registerAnnual(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_SUCCESS.getMessage());
    }

    // 사원 연차 수정
    @Operation(summary = "연차 수정", description = "사원이 본인의 연차 신청을 수정합니다.")
    @PutMapping("/emp/approval/annual/{annualId}")
    public ResponseEntity<String> updateAnnual(@PathVariable Long annualId,
                                               @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.updateAnnual(annualId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }

    // 사원 연차 삭제
    @Operation(summary = "연차 삭제", description = "사원이 본인의 연차 신청을 삭제합니다.")
    @DeleteMapping("/emp/approval/annual/{annualId}")
    public ResponseEntity<String> deleteAnnual(@PathVariable Long annualId) {
        approvalRequestCommandService.softDeleteAnnual(annualId);
        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }

    // 팀장 연차 승인
    @Operation(summary = "연차 승인", description = "팀장이 사원의 연차 신청을 승인합니다.")
    @PutMapping("/leader/approval/annual/{annualId}/approve")
    public ResponseEntity<String> approveAnnual(@PathVariable Long annualId, @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.approveAnnual(annualId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_CHECK_IN_SUCCESS.getMessage());
    }

    // 팀장 연차 반려
    @Operation(summary = "연차 반려", description = "팀장이 사원의 연차 신청을 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/approval/annual/{annualId}/reject")
    public ResponseEntity<String> rejectAnnual(@PathVariable Long annualId, @RequestBody String rejectReason) {
        approvalRequestCommandService.rejectAnnual(annualId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ANNUAL_CHECK_OUT_SUCCESS.getMessage());
    }*/

    // -------------------------- 초과근무 API --------------------------

    // 사원 초과근무 신청
    @Operation(summary = "초과근무 신청", description = "사원이 초과근무를 신청합니다.")
    @PostMapping("/emp/approval/overtimes")
    public ResponseEntity<SuccessCode> createOvertime(@RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.createOvertime(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_SUCCESS);
    }

    // 사원 초과근무 수정
    @Operation(summary = "초과근무 수정", description = "사원이 본인의 초과근무 신청을 수정합니다.")
    @PutMapping("/emp/approval/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> updateOvertime(@PathVariable Long overtimeId,
                                                      @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.updateOvertime(overtimeId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS);
    }

    // 사원 초과근무 삭제
    @Operation(summary = "초과근무 삭제", description = "사원이 본인의 초과근무 신청을 삭제합니다.")
    @DeleteMapping("/emp/approval/overtimes/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteOvertime(@PathVariable Long overtimeId) {
        approvalRequestCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS);
    }

    // 팀장 초과근무 승인
    @Operation(summary = "초과근무 승인", description = "팀장이 초과근무를 승인합니다.")
    @PutMapping("/leader/approval/overtimes/{overtimeId}/approve")
    public ResponseEntity<SuccessCode> approveOvertime(@PathVariable Long overtimeId, @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.approveOvertime(overtimeId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_APPROVED);
    }

    // 팀장 초과근무 반려
    @Operation(summary = "초과근무 반려", description = "팀장이 초과근무를 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/approval/overtimes/{overtimeId}/reject")
    public ResponseEntity<SuccessCode> rejectOvertime(@PathVariable Long overtimeId, @RequestBody String rejectReason) {
        approvalRequestCommandService.rejectOvertime(overtimeId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_OVERTIME_REJECTED);
    }

    // -------------------------- 휴가 API --------------------------

    // 사원 휴가 신청
    @Operation(summary = "휴가 신청", description = "사원이 휴가를 신청합니다.")
    @PostMapping("/emp/approval/vacation")
    public ResponseEntity<String> registerVacation(@RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.registerVacation(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_VACATION_SUCCESS.getMessage());
    }

    // 사원 휴가 수정
    @Operation(summary = "휴가 수정", description = "사원이 본인의 휴가 신청을 수정합니다.")
    @PutMapping("/emp/approval/vacation/{vacationId}")
    public ResponseEntity<String> updateVacation(@PathVariable Long vacationId, @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.updateVacation(vacationId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_VACATION_UPDATE_SUCCESS.getMessage());
    }

    // 사원 휴가 삭제
    @Operation(summary = "휴가 삭제", description = "사원이 본인의 휴가 신청을 삭제합니다.")
    @DeleteMapping("/emp/approval/vacation/{vacationId}")
    public ResponseEntity<String> deleteVacation(@PathVariable Long vacationId) {
        approvalRequestCommandService.deleteVacation(vacationId);
        return ResponseEntity.ok(SuccessCode.WORK_ATTRIBUTE_VACATION_DELETE_SUCCESS.getMessage());
    }

    // 팀장 휴가 승인
    @Operation(summary = "휴가 승인", description = "팀장이 사원의 휴가 신청을 승인합니다.")
    @PutMapping("/leader/approval/vacation/{vacationId}/approve")
    public ResponseEntity<String> approveVacation(@PathVariable Long vacationId, @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.approveVacation(vacationId, requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_VACATION_CHECK_IN_SUCCESS.getMessage());
    }

    // 팀장 휴가 반려
    @Operation(summary = "휴가 반려", description = "팀장이 사원의 휴가 신청을 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/approval/vacation/{vacationId}/reject")
    public ResponseEntity<String> rejectVacation(@PathVariable Long vacationId, @RequestBody String rejectReason) {
        approvalRequestCommandService.rejectVacation(vacationId, rejectReason);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_VACATION_CHECK_OUT_SUCCESS.getMessage());
    }

    // -------------------------- 출장 API --------------------------

    // 사원 출장 신청
    @Operation(summary = "출장 신청", description = "사원이 출장 신청을 합니다.")
    @PostMapping("/emp/approval/travels")
    public ResponseEntity<SuccessCode> requestTravel(@RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.requestTravel(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_TRAVEL_SUCCESS);
    }

    // 사원 출장 수정
    @Operation(summary = "출장 수정", description = "사원이 본인의 출장 신청을 수정합니다.")
    @PutMapping("/emp/approval/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravel(@PathVariable Long travelId, @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.updateTravel(travelId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 사원 출장 삭제
    @Operation(summary = "출장 삭제", description = "사원이 본인의 출장 신청을 삭제합니다.")
    @DeleteMapping("/emp/approval/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteTravel(@PathVariable Long travelId) {
        approvalRequestCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 팀장 출장 상태 변경
    @Operation(summary = "출장 상태 변경 결재", description = "팀장이 사원의 출장 신청을 승인/반려합니다.")
    @PutMapping("/leader/approval/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravelStatus(@PathVariable Long travelId,
                                                          @RequestBody WorkAttitudeApprovalRequestDTO requestDTO) {
        approvalRequestCommandService.updateTravelStatus(travelId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
