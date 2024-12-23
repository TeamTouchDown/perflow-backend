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

@Tag(name = "WorkAttitude-Annual-Controller", description = "출퇴근 관련 API")
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
    public ResponseEntity<List<WorkAttitudeAnnualResponseDTO>> getAnnualList(@RequestParam String empId) {
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
}

// 연차 오전반차 오후 반차 사용 횟수 조회 종류별로 조회
// 사원 개인의 연차 오전 반차 오후 반차 잔여 횟수 조회 오전 반차 오후 반차는 연차의 갯수의 절반으로 계산하고
// 입사년도 기준으로 기본 15일 3년차마다 +1일씩 계산해서 사원별로 전체 연차 갯수가 정해지게 계산
// 사원 기준 연차 모든 부분을 조회할 수 있는데 신청일 시작일 종료일 상태(결재가 되었는지 확인하는 용도) 결재자 누군지 리스폰스하도록 만들게
// 팀장은 해당 부서 사원의 모든 연차 내역을 조회 -> 부서를 필터링 해서 본인 포함 팀원의 연차 관련 내용을 응답
// 인사팀은 모든 사원의 연차 내역을 조회 신청일 시작일 종료일 결재상태 연차 구분(연차, 오전, 오후)