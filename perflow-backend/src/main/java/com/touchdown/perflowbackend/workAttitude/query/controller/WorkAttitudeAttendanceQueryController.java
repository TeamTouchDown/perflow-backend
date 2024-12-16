package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeAttendanceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "WorkAttitude-Attendance-Controller", description = "출퇴근 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeAttendanceQueryController {

    private final WorkAttitudeAttendanceQueryService attendanceQueryService;

    // 사원 주차별 조회 (empId는 자동으로 서비스에서 처리)
    @Operation(summary = "사원 주차별 근무 시간 조회", description = "사원 본인의 주차별 근무 시간을 조회합니다.")
    @GetMapping("/emp/attendance/summaries/weekly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getWeeklySummaryForEmployee() {
        return ResponseEntity.ok(attendanceQueryService.getWeeklySummaryForEmployee());
    }

    // 사원 월별 조회 (empId는 자동으로 서비스에서 처리)
    @Operation(summary = "사원 월별 근무 시간 조회", description = "사원 본인의 월별 근무 시간을 조회합니다.")
    @GetMapping("/emp/attendance/summaries/monthly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getMonthlySummaryForEmployee() {
        return ResponseEntity.ok(attendanceQueryService.getMonthlySummaryForEmployee());
    }

    // 팀장 주차별 조회
    @Operation(summary = "팀장 주차별 근무 시간 조회", description = "팀장은 팀원의 주차별 근무 시간을 조회합니다.")
    @GetMapping("/leader/attendance/summaries/weekly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getWeeklySummaryForTeam() {
        return ResponseEntity.ok(attendanceQueryService.getWeeklySummaryForTeam());
    }

    // 팀장 월별 조회
    @Operation(summary = "팀장 월별 근무 시간 조회", description = "팀장은 팀원의 월별 근무 시간을 조회합니다.")
    @GetMapping("/leader/attendance/summaries/monthly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getMonthlySummaryForTeam() {
        return ResponseEntity.ok(attendanceQueryService.getMonthlySummaryForTeam());
    }

    // 인사팀 주차별 조회
    @Operation(summary = "인사팀 주차별 근무 시간 조회", description = "인사팀은 모든 사원의 주차별 근무 시간을 조회합니다.")
    @GetMapping("/hr/attendance/summaries/weekly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getWeeklySummaryForAllEmployees() {
        return ResponseEntity.ok(attendanceQueryService.getWeeklySummaryForAllEmployees());
    }

    // 인사팀 월별 조회
    @Operation(summary = "인사팀 월별 근무 시간 조회", description = "인사팀은 모든 사원의 월별 근무 시간을 조회합니다.")
    @GetMapping("/hr/attendance/summaries/monthly")
    public ResponseEntity<List<WorkAttitudeAttendanceSummaryResponseDTO>> getMonthlySummaryForAllEmployees() {
        return ResponseEntity.ok(attendanceQueryService.getMonthlySummaryForAllEmployees());
    }
}
