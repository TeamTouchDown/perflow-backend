package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeTravelCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class WorkAttitudeTravelCommandController {

    private final WorkAttitudeTravelCommandService workAttitudeTravelCommandService;

    @PostMapping("/emp/travel")
    public ResponseEntity<SuccessCode>requestTravel(@RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO){
        workAttitudeTravelCommandService.createTravel(workAttitudeTravelRequestDTO);
        return ResponseEntity.ok(SuccessCode.WAT_SUCCESS) ;
    }
}
/*
*
    // ===================== 사원용 API =====================

    // 1. 출장 등록 (사원)
    @PostMapping("/emp/travel")
    public ResponseEntity<String> requestTravel(@RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.createTravel(workAttitudeTravelRequestDTO);
        return ResponseEntity.ok("출장 요청이 등록되었습니다.");
    }

    // 2. 출장 조회 (사원)
    @GetMapping("/emp/travel/{travel_id}")
    public ResponseEntity<WorkAttitudeTravelRequestDTO> getTravel(@PathVariable Long travel_id) {
        WorkAttitudeTravelRequestDTO dto = workAttitudeTravelCommandService.getTravelById(travel_id);
        return ResponseEntity.ok(dto);
    }

    // 3. 출장 수정 (사원)
    @PutMapping("/emp/travel/{travel_id}")
    public ResponseEntity<String> updateTravel(
            @PathVariable Long travel_id,
            @RequestBody WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        workAttitudeTravelCommandService.updateTravel(travel_id, workAttitudeTravelRequestDTO);
        return ResponseEntity.ok("출장 정보가 수정되었습니다.");
    }

    // 4. 출장 삭제 (사원)
    @DeleteMapping("/emp/travel/{travel_id}")
    public ResponseEntity<String> deleteTravel(@PathVariable Long travel_id) {
        workAttitudeTravelCommandService.deleteTravel(travel_id);
        return ResponseEntity.ok("출장 요청이 삭제되었습니다.");
    }

    // ===================== 팀장용 API =====================

    // 5. 출장 요청 결재 (팀장)
    @PutMapping("/leader/travel/{travel_id}")
    public ResponseEntity<String> approveTravel(
            @PathVariable Long travel_id,
            @RequestBody String approvalStatus) {
        workAttitudeTravelCommandService.approveTravel(travel_id, approvalStatus);
        return ResponseEntity.ok("출장 요청이 결재되었습니다.");
    }

    // 6. 출장 요청 조회 (팀장)
    @GetMapping("/leader/travel")
    public ResponseEntity<List<WorkAttitudeTravelRequestDTO>> getAllTravelRequests() {
        List<WorkAttitudeTravelRequestDTO> requests = workAttitudeTravelCommandService.getAllTravelRequests();
        return ResponseEntity.ok(requests);
    }

    // 7. 출장 요청 삭제 (팀장)
    @DeleteMapping("/leader/travel/{travel_id}")
    public ResponseEntity<String> deleteTravelRequest(@PathVariable Long travel_id) {
        workAttitudeTravelCommandService.deleteTravel(travel_id);
        return ResponseEntity.ok("출장 요청이 삭제되었습니다.");
    }
}
*/