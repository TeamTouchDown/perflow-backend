package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeAnnualQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "WorkAttitude-Annual-Controller", description = "연차 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeAnnualQueryController {

    private final WorkAttitudeAnnualQueryService workAttitudeAnnualQueryService;

    // 직원: 연차 사용 횟수 조회 (종류별)
    @Operation(summary = "직원 연차 사용 횟수 조회", description = "직원의 연차 사용 횟수를 조회합니다.")
    @GetMapping("/emp/annual/usage")
    public ResponseEntity<?> getAnnualUsage() {
        return ResponseEntity.ok(workAttitudeAnnualQueryService.getAnnualUsage());
    }

    // 직원: 연차 잔여 횟수 조회
    @Operation(summary = "직원 연차 잔여 횟수 조회", description = "직원의 연차 잔여 횟수를 조회합니다.")
    @GetMapping("/emp/annual/balance")
    public ResponseEntity<?> getAnnualBalance() {
        return ResponseEntity.ok(workAttitudeAnnualQueryService.getAnnualBalance());
    }

    // 직원: 연차 전체 조회
    @Operation(summary = "직원 연차 내역 조회", description = "직원의 연차 내역을 전체 조회합니다.")
    @GetMapping("/emp/annual/list")
    public ResponseEntity<List<WorkAttitudeAnnualResponseDTO>> getAnnualList() {
        return ResponseEntity.ok(workAttitudeAnnualQueryService.getAnnualList());
    }

    // 팀장: 부서 내 연차 내역 조회
    @Operation(summary = "팀장 부서 연차 내역 조회", description = "팀장이 부서 내 직원들의 연차 내역을 조회합니다.")
    @GetMapping("/leader/annual/team")
    public ResponseEntity<List<WorkAttitudeAnnualResponseDTO>> getTeamAnnualList() {
        return ResponseEntity.ok(workAttitudeAnnualQueryService.getTeamAnnualList());
    }

    // 인사팀: 모든 사원의 연차 내역 조회
    @Operation(summary = "인사팀 전체 연차 내역 조회", description = "인사팀이 모든 사원의 연차 내역을 조회합니다.")
    @GetMapping("/hr/annual/all")
    public ResponseEntity<List<WorkAttitudeAnnualResponseDTO>> getAllAnnualList() {
        return ResponseEntity.ok(workAttitudeAnnualQueryService.getAllAnnualList());
    }

    // 직원: 연차 사용 내역 종류별 조회 (유저 ID 포함)
    @Operation(summary = "직원 연차 사용 내역 조회 (종류별)", description = "직원이 사용한 연차의 종류별 사용 내역을 조회합니다.")
    @GetMapping("/emp/annual/usage/details")
    public ResponseEntity<Map<String, Object>> getAnnualUsageDetails(@RequestParam String empId) {
        Map<String, Object> response = workAttitudeAnnualQueryService.getAnnualUsageDetails();
        return ResponseEntity.ok(response);
    }
}
