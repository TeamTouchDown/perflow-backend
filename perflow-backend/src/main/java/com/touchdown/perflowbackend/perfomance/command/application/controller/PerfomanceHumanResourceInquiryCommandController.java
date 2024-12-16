package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateInquiryRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.HumanResourceCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances")
@RequiredArgsConstructor
public class PerfomanceHumanResourceInquiryCommandController {

    private final HumanResourceCommandService humanResourceCommandService;

    // 인사 평가 의의제기 생성
    @PostMapping("/inquiry/{empId}/{hrperfoId}")
    public ResponseEntity<SuccessCode> createPerfoInquiry(
            @PathVariable("empId") String empId,
            @PathVariable("hrperfoId") Long hrperfoId,
            @RequestParam("reason") String reason ) {

        humanResourceCommandService.createHrPerfoInquiry(empId, hrperfoId, reason);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_UPLOAD_SUCCESS);
    }

    // 인사 평가 의의제기 수정(승인)
    @PutMapping("hr/inquiry/approve/{empId}/{hrperfoInquiryId}")
    public ResponseEntity<SuccessCode> updatePerfoInquiry(
            @PathVariable("empId") String empId,
            @PathVariable("hrperfoInquiryId") Long hrperfoInquiryId,
            @RequestBody UpdateInquiryRequestDTO updateInquiryRequestDTO) {

        humanResourceCommandService.updateHrPerfoInquiry(empId,hrperfoInquiryId,updateInquiryRequestDTO);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_UPDATE_SUCCESS);
    }

    // 인사 평가 의의제기 수정(반려)
    @PutMapping("hr/inquiry/reject/{empId}/{hrperfoInquiryId}")
    public ResponseEntity<SuccessCode> rejectPerfoInquiry(
            @PathVariable("empId") String empId,
            @PathVariable("hrperfoInquiryId") Long hrperfoInquiryId,
            @RequestParam String reason) {

        humanResourceCommandService.updateHrPerfoInquiry(empId, hrperfoInquiryId, reason);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_UPDATE_SUCCESS);
    }
}
