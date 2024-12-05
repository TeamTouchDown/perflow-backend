package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}