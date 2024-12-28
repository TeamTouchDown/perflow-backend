package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeTravelQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WorkAttitude-Controller", description = "출장 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeTravelQueryController {

    private final WorkAttitudeTravelQueryService workAttitudeTravelQueryService;

    @Operation(summary = "사원 - 본인 출장 전체 조회")
    @GetMapping("/emp/travels")
    public ResponseEntity<List<WorkAttitudeTravelResponseDTO>> getTravelsForEmployee() {
        List<WorkAttitudeTravelResponseDTO> dtoList = workAttitudeTravelQueryService.getTravelsForEmployee();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "팀장 - 본인 결재 대상 포함, 전체 출장 조회")
    @GetMapping("/leader/travels")
    public ResponseEntity<List<WorkAttitudeTravelResponseDTO>> getAllTravelsForLeader() {
        List<WorkAttitudeTravelResponseDTO> dtoList = workAttitudeTravelQueryService.getAllTravelsForLeader();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "팀장 - 결재해야 할(미결) 출장 목록 조회")
    @GetMapping("/leader/travels/pending")
    public ResponseEntity<List<WorkAttitudeTravelResponseDTO>> getPendingTravelsForLeader() {
        List<WorkAttitudeTravelResponseDTO> dtoList = workAttitudeTravelQueryService.getPendingTravelsForLeader();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "인사팀 - 모든 사원의 출장 조회")
    @GetMapping("/hr/travels")
    public ResponseEntity<List<WorkAttitudeTravelResponseDTO>> getAllTravelsForHR() {
        List<WorkAttitudeTravelResponseDTO> dtoList = workAttitudeTravelQueryService.getAllTravelsForHR();
        return ResponseEntity.ok(dtoList);
    }
}
