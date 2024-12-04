package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class WorkAttitudeTravelCommandController {

    private final WorkAttitudeTravelCommandService workAttitudeTravelCommandService;

    //출장 등록(사원)
    @PostMapping("/emp/travel")
    public ResponseEntity<SuccessCode>requestTravel(@RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO){
        workAttitudeTravelCommandService.createTravel(workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.WAT_SUCCESS) ;
    }
    //출장 조회(사원)
    @GetMapping("/emp/travel/{travel_id}")
    public ResponseEntity<WorkAttitudeTravelRequestDTO> getTravel(@PathVariable Long travel_id) {
        WorkAttitudeTravelRequestDTO dto = workAttitudeTravelCommandService.getTravelById(travel_id);
        return ResponseEntity.ok(dto);
    }

    // 3. 출장 수정 (사원)
    @PutMapping("/emp/travel/{travel_id}")
    public ResponseEntity<SuccessCode> updateTravel(
            @PathVariable Long travel_id,
            @RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.updateTravel(travel_id, workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
    // 4. 출장 삭제 (사원)
    @DeleteMapping("/emp/travel/{travel_id}")
    public ResponseEntity<SuccessCode> deleteTravel(@PathVariable Long travel_id) {
        workAttitudeTravelCommandService.deleteTravel(travel_id);
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
    //------ 팀장 부분 ------



}