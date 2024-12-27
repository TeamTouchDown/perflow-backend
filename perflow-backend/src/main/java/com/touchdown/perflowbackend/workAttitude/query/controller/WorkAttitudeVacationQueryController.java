package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeVacationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "WorkAttitude-Vacation-Controller", description = "휴가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeVacationQueryController {

    private final WorkAttitudeVacationQueryService workAttitudeVacationQueryService;

    // 사원 개인의 사용한 휴가 조회 (종류별)
    @Operation(summary = "사원 개인 휴가 조회", description = "사원이 사용한 휴가 정보를 조회합니다.")
    @GetMapping("/emp/vacation/usage")
    public ResponseEntity<Map<String, Object>> getVacationUsage() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getVacationUsageDetails());
    }

    // 사원 개인의 휴가 상세 조회
    @Operation(summary = "사원 휴가 상세 조회", description = "사원의 휴가 상세 정보를 조회합니다.")
    @GetMapping("/emp/vacation/details")
    public ResponseEntity<List<WorkAttitudeVacationResponseDTO>> getVacationDetails() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getVacationDetails());
    }

    // 가장 가까운 다가오는 휴가일 조회
    @Operation(summary = "다가오는 휴가일 조회", description = "현재 날짜 기준 가장 가까운 휴가일과 남은 일수를 조회합니다.")
    @GetMapping("/emp/vacation/nearest")
    public ResponseEntity<Map<String, Object>> getNearestVacation() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getNearestVacation());
    }

    // 팀장의 팀원 휴가 조회 (본인 포함)
    @Operation(summary = "팀원 휴가 조회", description = "팀장이 팀원의 휴가 내역을 조회합니다.")
    @GetMapping("/leader/vacation/team")
    public ResponseEntity<List<WorkAttitudeVacationResponseDTO>> getTeamVacationList() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getTeamVacationList());
    }

    // 인사팀 전체 휴가 내역 조회
    @Operation(summary = "전체 휴가 내역 조회", description = "인사팀이 모든 직원의 휴가 내역을 조회합니다.")
    @GetMapping("/hr/vacation/all")
    public ResponseEntity<List<WorkAttitudeVacationResponseDTO>> getAllVacationList() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getAllVacationList());
    }
}
