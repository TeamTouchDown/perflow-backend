package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForEmployeeSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeOvertimeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "WorkAttitude-OverTime-Controller", description = "초과근무 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeOvertimeQueryController {

    private final WorkAttitudeOvertimeQueryService workAttitudeOvertimeQueryService;

    // 팀장 전체 조회
    @Operation(summary = "팀장 전체 조회", description = "팀장이 모든 직원의 초과근무 요약 정보를 조회합니다.")
    @GetMapping("/leader/overtime/summary")
    public ResponseEntity<List<WorkAttitudeOvertimeForTeamLeaderSummaryDTO>> getOvertimeSummaryForAllEmployees() {
        List<WorkAttitudeOvertimeForTeamLeaderSummaryDTO> summaries = workAttitudeOvertimeQueryService.getOvertimeSummaryForAllEmployees();
        return ResponseEntity.ok(summaries);
    }

    // 사원 본인 조회

    @Operation(summary = "사원 본인 조회", description = "사원이 자신의 초과근무 요약 정보를 조회")
    @GetMapping("/employee/overtime/summary")
    public ResponseEntity<WorkAttitudeOvertimeForEmployeeSummaryDTO> getOvertimeSummaryForEmployee() {
        String empId = EmployeeUtil.getEmpId(); // 인증된 사원 ID 가져오기
        WorkAttitudeOvertimeForEmployeeSummaryDTO summary = workAttitudeOvertimeQueryService.getOvertimeSummaryForEmployee(empId);
        return ResponseEntity.ok(summary);
    }



}
