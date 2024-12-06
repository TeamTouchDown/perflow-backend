package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelCommandForTeamLeaderRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "WorkAttribute-Controller", description = "출장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeTravelCommandController {

    private final WorkAttitudeTravelCommandService workAttitudeTravelCommandService;

    //출장 등록(사원)
    @PostMapping("/emp/travels")
    public ResponseEntity<SuccessCode>requestTravel(@RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO){
        workAttitudeTravelCommandService.createTravel(workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_TRAVEL_SUCCESS) ;
    }

    // 3. 출장 수정 (사원)
    @PutMapping("/emp/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravel(
            @PathVariable Long travelId,
            @RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.updateTravel(travelId, workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
    // 4. 출장 삭제 (사원)
    @DeleteMapping("/emp/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteTravel(@PathVariable(name="travelId") Long travelId) {
        workAttitudeTravelCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
    //------ 팀장 부분 ------
    // 팀장: 출장 상태 변경 (승인/반려)
    @PutMapping("/leader/travel/{travelId}")
    public ResponseEntity<SuccessCode> updateTravelStatus(
            @PathVariable Long travelId,
            @RequestBody WorkAttitudeTravelCommandForTeamLeaderRequestDTO requestDTO) {
        workAttitudeTravelCommandService.updateTravelStatus(travelId, requestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    // 팀장: 출장 삭제
    @DeleteMapping("/leader/travel/{travelId}")
    public ResponseEntity<SuccessCode> deleteEmployeeTravel(@PathVariable Long travelId) {
        workAttitudeTravelCommandService.deleteTravel(travelId);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }








}