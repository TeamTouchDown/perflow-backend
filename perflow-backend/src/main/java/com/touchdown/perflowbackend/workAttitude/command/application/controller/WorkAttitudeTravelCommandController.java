package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "WorkAttitude-Travel-Controller", description = "출장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeTravelCommandController {

    private final WorkAttitudeTravelCommandService travelCommandService;


    @Operation(summary = "출장 신청", description = "사원이 출장 신청을 합니다.")
    @PostMapping("/emp/approval/travels")
    public ResponseEntity<SuccessCode> requestTravel(@RequestBody WorkAttitudeTravelRequestDTO requestDTO) {
        travelCommandService.requestTravel(requestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_TRAVEL_SUCCESS);
    }


    @Operation(summary = "출장 수정", description = "사원이 본인의 출장 신청을 수정합니다.")
    @PutMapping("/emp/approval/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravel(@PathVariable Long travelId,
                                                    @RequestBody WorkAttitudeTravelRequestDTO requestDTO) {
        travelCommandService.updateTravel(travelId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @Operation(summary = "출장 삭제", description = "사원이 본인의 출장 신청을 삭제합니다.")
    @DeleteMapping("/emp/approval/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteTravel(@PathVariable Long travelId) {
        travelCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }


    @Operation(summary = "출장 승인", description = "팀장이 사원의 출장 신청을 승인합니다.")
    @PutMapping("/leader/approval/travel/{travelId}/approve")
    public ResponseEntity<SuccessCode> approveTravel(@PathVariable Long travelId) {
        // 팀장이 승인할 때 따로 DTO가 필요 없다면, travelId만 받음
        travelCommandService.approveTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }


    @Operation(summary = "출장 반려", description = "팀장이 사원의 출장 신청을 반려합니다. 반려 사유를 작성해야 합니다.")
    @PutMapping("/leader/approval/travel/{travelId}/reject")
    public ResponseEntity<SuccessCode> rejectTravel(@PathVariable Long travelId,
                                                    @RequestBody String rejectReason) {
        travelCommandService.rejectTravel(travelId, rejectReason);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
