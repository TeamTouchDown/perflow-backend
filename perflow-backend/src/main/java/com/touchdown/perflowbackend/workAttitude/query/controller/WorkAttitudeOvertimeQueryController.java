package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeOvertimeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "WorkAttitude-OverTime-Controller", description = "초과근무 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeOvertimeQueryController {

    private final WorkAttitudeOvertimeQueryService workAttitudeOvertimeQueryService;

    // 사원 본인 초과근무 내역 조회
    @Operation(summary = "사원 본인 조회", description = "사원이 자신의 초과근무 내역을 조회")
    @GetMapping("/emp/overtimes")
    public ResponseEntity<List<WorkAttitudeOvertimeResponseDTO>> getOvertimeForEmployee() {
        String empId = EmployeeUtil.getEmpId(); // 인증된 사원 ID 가져오기
        List<WorkAttitudeOvertimeResponseDTO> response = workAttitudeOvertimeQueryService.getOvertimeForEmployee(empId);
        return ResponseEntity.ok(response);
    }

    // 사원 본인의 초과근무 유형별 요약 조회
    @Operation(summary = "사원 월별 초과근무 요약 조회", description = "사원이 자신의 월별 초과근무 유형별 요약 정보를 조회")
    @GetMapping("/emp/overtimes/monthly-summary")
    public ResponseEntity<Map<String, Map<String, String>>> getMonthlyOvertimeSummaryForEmployee() {
        String empId = EmployeeUtil.getEmpId(); // 현재 로그인된 사원 ID 조회
        Map<String, Map<String, String>> summary = workAttitudeOvertimeQueryService.getMonthlyOvertimeSummary(empId); // 서비스 호출
        return ResponseEntity.ok(summary); // 결과 반환

    }

    // 팀장의 팀원 초과근무 내역 조회
    @Operation(summary = "팀원 초과근무 조회", description = "팀장이 팀원의 초과근무 내역을 조회")
    @GetMapping("/leader/overtimes/team")
    public ResponseEntity<List<WorkAttitudeOvertimeResponseDTO>> getOvertimeForTeam() {
        List<WorkAttitudeOvertimeResponseDTO> response = workAttitudeOvertimeQueryService.getOvertimeForTeam();
        return ResponseEntity.ok(response);
    }



    // 인사팀의 전체 초과근무 내역 조회
    @Operation(summary = "전체 초과근무 조회", description = "인사팀이 전체 직원의 초과근무 내역을 조회")
    @GetMapping("/hr/overtimes/all")
    public ResponseEntity<List<WorkAttitudeOvertimeResponseDTO>> getAllOvertimes() {
        List<WorkAttitudeOvertimeResponseDTO> response = workAttitudeOvertimeQueryService.getAllOvertimes();
        return ResponseEntity.ok(response);
    }
}
