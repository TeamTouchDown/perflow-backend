package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelCommandForTeamLeaderRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-Controller", description = "출장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeTravelCommandController {

    private final WorkAttitudeTravelCommandService workAttitudeTravelCommandService;

    @Operation(summary = "사원이 출장 등록", description = "사원이 자신의 출장 요청을 등록")
    @PostMapping("/emp/travels")
    public ResponseEntity<SuccessCode> requestTravel(
            @RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.createTravel(workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_TRAVEL_SUCCESS);
    }

    @Operation(summary = "사원이 출장 수정", description = "사원이 자신의 출장 요청을 수정")
    @PutMapping("/emp/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravel(
            @PathVariable Long travelId,
            @RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.updateTravel(travelId, workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @Operation(summary = "사원이 출장 삭제", description = "사원이 자신의 출장 요청을 삭제")
    @DeleteMapping("/emp/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteTravel(
            @PathVariable(name = "travelId") Long travelId) {
        workAttitudeTravelCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @Operation(summary = "팀장이 출장 상태 변경", description = "팀장이 사원의 출장 요청 상태를 승인, 반려함 반려시 반려사유도 작성 가능")
    @PutMapping("/leader/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravelStatus(
            @PathVariable(name = "travelId") Long travelId,
            @RequestBody WorkAttitudeTravelCommandForTeamLeaderRequestDTO requestDTO) {
        workAttitudeTravelCommandService.updateTravelStatus(travelId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @Operation(summary = "팀장이 출장 삭제", description = "팀장이 사원의 출장 요청을 삭제")
    @DeleteMapping("/leader/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteEmployeeTravel(
            @PathVariable(name = "travelId") Long travelId) {
        workAttitudeTravelCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
