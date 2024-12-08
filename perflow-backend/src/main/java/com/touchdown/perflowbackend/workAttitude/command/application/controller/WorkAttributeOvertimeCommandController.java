package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttributeOvertimeCommandService;

import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping("/emp/overtimes")
    public ResponseEntity<SuccessCode> createOvertime(@RequestBody WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        workAttributeOvertimeCommandService.createOvertime(workAttributeOvertimeForEmployeeRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 직원: 초과근무 수정
    @PutMapping("/emp/{overtimeId}")
    public ResponseEntity<SuccessCode> updateOvertime(@PathVariable Long overtimeId,
                                                      @RequestBody WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        workAttributeOvertimeCommandService.updateOvertime(overtimeId, workAttributeOvertimeForEmployeeRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 직원: 초과근무 삭제
    @DeleteMapping("/emp/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteOvertime(@PathVariable Long overtimeId) {
        workAttributeOvertimeCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 팀장: 초과근무 상태 변경 (승인/반려)
    @PutMapping("/leader/{overtimeId}/status")
    public ResponseEntity<SuccessCode> updateOvertimeStatus(@PathVariable Long overtimeId,
                                                            @RequestBody WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        workAttributeOvertimeCommandService.updateOvertimeStatus(overtimeId, workAttributeOvertimeForEmployeeRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 팀장: 팀원 초과근무 요청 삭제
    @DeleteMapping("/leader/{overtimeId}")
    public ResponseEntity<SuccessCode> deleteTeamOvertime(@PathVariable Long overtimeId) {
        workAttributeOvertimeCommandService.deleteOvertime(overtimeId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }



}
