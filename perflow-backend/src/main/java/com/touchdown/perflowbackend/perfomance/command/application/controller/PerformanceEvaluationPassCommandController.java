package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.infrastructure.repository.JpaCompanyCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreatePerfoAdjustmentDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.EvalutionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/perfo")
@RequiredArgsConstructor
public class PerformanceEvaluationPassCommandController {

    private final EvalutionCommandService evalutionCommandService;

    // 평가 조정 생성
    @PostMapping("adjustment/{perfoId}/{perfoedId}")
    public ResponseEntity<SuccessCode> CreateperfoAdjustment(
            @PathVariable("perfoId") String perfoId,
            @PathVariable("perfoedId") String perfoedId,
            @RequestBody CreatePerfoAdjustmentDTO createPerfoAdjustmentDTO) {

        evalutionCommandService.createPerfoAdjustment(perfoId, perfoedId, createPerfoAdjustmentDTO);

        return ResponseEntity.ok(SuccessCode.EVALUTION_ADJUSTMENT_UPDATE_SUCCESS);

    }
}
