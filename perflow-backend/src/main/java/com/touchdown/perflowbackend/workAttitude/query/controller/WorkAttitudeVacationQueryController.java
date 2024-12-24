package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeVacationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WorkAttitude-Vacation-Controller", description = "휴가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeVacationQueryController {

    private final WorkAttitudeVacationQueryService workAttitudeVacationQueryService;

    // 사원 개인의 사용한 휴가 조회 (종류별)
    @Operation(summary = "사원 개인 휴가 조회", description = "사원이 사용한 휴가 정보를 조회합니다.")
    @GetMapping("/emp/vacation/usage")
    public ResponseEntity<?> getVacationUsage() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getVacationUsage());
    }

    // 사원 개인의 휴가 상세 조회
    @Operation(summary = "사원 휴가 상세 조회", description = "사원의 휴가 상세 정보를 조회합니다.")
    @GetMapping("/emp/vacation/details")
    public ResponseEntity<List<WorkAttitudeVacationResponseDTO>> getVacationDetails() {
        return ResponseEntity.ok(workAttitudeVacationQueryService.getVacationDetails());
    }

    // 팀장의 팀원 휴가 조회
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

/*
* 사원 개인의 사용한 휴가 조회 종류 별로
* 사원 개인의 휴가 시작일, 종료일, 신청일, 결재 상태, 결재자, 반려 사유(있으면) 조회
* 팀장의 해당 팀원의 휴가 내역 조회 모든 상태를
* 인사팀은 모든 직원의 휴가 내역 조회
* */