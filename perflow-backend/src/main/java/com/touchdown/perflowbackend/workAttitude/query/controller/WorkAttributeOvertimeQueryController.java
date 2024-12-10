package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttributeOvertimeForEmployeeResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttributeOvertimeForTeamLeaderResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttributeOvertimeQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "WorkAttribute-Controller", description = "출장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttributeOvertimeQueryController {

    private final WorkAttributeOvertimeQueryService workAttributeOvertimeQueryService;

    //개인 초과근무 조회
    @GetMapping("/emp/Overtimes")
    public ResponseEntity<List<WorkAttributeOvertimeForEmployeeResponseDTO>>getOvertimesForEmployee(){
        List<WorkAttributeOvertimeForEmployeeResponseDTO>dto = workAttributeOvertimeQueryService.getOvertimesForEmployee();
        return ResponseEntity.ok(dto);
    }

    //개인 특정 유형 초과근무 조회
    @GetMapping("/emp/overtimes/type")
    public ResponseEntity<List<WorkAttributeOvertimeForEmployeeResponseDTO>> getOvertimesForEmployeeByType(String overtimeType) {
        List<WorkAttributeOvertimeForEmployeeResponseDTO> dto = workAttributeOvertimeQueryService.getOvertimesForEmployeeByType(overtimeType);
        return ResponseEntity.ok(dto);
    }

    // 팀장 초과근무 전체 조회
    @GetMapping("/leader/Overtimes")
    public ResponseEntity<List<WorkAttributeOvertimeForTeamLeaderResponseDTO>>getAllOvertimesForLeader(){
        List<WorkAttributeOvertimeForTeamLeaderResponseDTO>dto = workAttributeOvertimeQueryService.getAllOvertimeForLeader();
        return ResponseEntity.ok(dto);
    }

    // 팀장: 특정 유형 초과근무 조회
    @GetMapping("/leader/overtimes/type")
    public ResponseEntity<List<WorkAttributeOvertimeForTeamLeaderResponseDTO>> getOvertimesByTypeForLeader(@RequestParam("type") String overtimeType) {
        List<WorkAttributeOvertimeForTeamLeaderResponseDTO> dtoList = workAttributeOvertimeQueryService.getOvertimesByTypeForLeader(overtimeType);
        return ResponseEntity.ok(dtoList);
    }

}
