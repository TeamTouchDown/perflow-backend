package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreatePerfoRatioRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.RatioCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/ratio")
@RequiredArgsConstructor
public class PerfomancceRatioCommandController {

    private final RatioCommandService ratioCommandService;

    // 인사 평가 반영 비율 생성
    @PostMapping("/perfo/{empId}/{deptId}")
    public ResponseEntity<SuccessCode> createPerfoRatio(
            @PathVariable("empId") String empId,
            @PathVariable("deptId") Long deptId,
            @RequestBody CreatePerfoRatioRequestDTO createPerfoRatioRequestDTO) {

        ratioCommandService.createPerfoRatio(empId, deptId, createPerfoRatioRequestDTO);

        return ResponseEntity.ok(SuccessCode.RATIO_PERFO_UPLOAD_SUCCESS);
    }
}
