package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAnnualCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-Annual-Controller", description = "연차 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeAnnualCommandController {

    private final WorkAttitudeAnnualCommandService annualCommandService;

    // 사원 연차 관련 API
    @Operation(summary = "연차 신청", description = "사원이 연차 신청합니다. 사후 신청 여부 포함.")
    @PostMapping("/emp/annual")
    public ResponseEntity<String> registerAnnual(@RequestBody WorkAttitudeAnnualRequestDTO requestDTO,
                                                 @RequestParam Boolean isAnnualRetroactive) {
        requestDTO.setIsAnnualRetroactive(isAnnualRetroactive);
        annualCommandService.registerAnnual(requestDTO);
        return ResponseEntity.ok("연차 신청 완료");
    }

    @Operation(summary = "연차 수정", description = "사원이 연차를 수정합니다.")
    @PutMapping("/emp/annual/{annualId}")
    public ResponseEntity<String> updateAnnual(@PathVariable Long annualId,
                                               @RequestBody WorkAttitudeAnnualRequestDTO requestDTO) {
        annualCommandService.updateAnnual(annualId, requestDTO);
        return ResponseEntity.ok("연차 수정 완료");
    }

    @Operation(summary = "연차 삭제", description = "사원이 연차를 삭제합니다.")
    @DeleteMapping("/emp/annual/{annualId}")
    public ResponseEntity<String> deleteAnnual(@PathVariable Long annualId) {
        annualCommandService.softDeleteAnnual(annualId);
        return ResponseEntity.ok("연차 삭제 완료");
    }

    // 팀장 연차 관련 API
    @Operation(summary = "연차 승인", description = "팀장이 연차를 승인합니다.")
    @PutMapping("/leader/annual/{annualId}/approve")
    public ResponseEntity<String> approveAnnual(@PathVariable Long annualId) {
        annualCommandService.approveAnnual(annualId);
        return ResponseEntity.ok("연차 승인 완료");
    }

    @Operation(summary = "연차 반려", description = "팀장이 연차를 반려합니다.")
    @PutMapping("/leader/annual/{annualId}/reject")
    public ResponseEntity<String> rejectAnnual(@PathVariable Long annualId, @RequestBody String rejectReason) {
        annualCommandService.rejectAnnual(annualId, rejectReason);
        return ResponseEntity.ok("연차 반려 완료");
    }

    @Operation(summary = "연차 반려 사유 작성", description = "팀장이 연차 반려 사유를 작성합니다.")
    @PostMapping("/leader/annual/{annualId}/reject-reason")
    public ResponseEntity<String> writeRejectReason(@PathVariable Long annualId, @RequestBody String rejectReason) {
        annualCommandService.rejectAnnual(annualId, rejectReason);
        return ResponseEntity.ok("연차 반려 사유 작성 완료");
    }
}
