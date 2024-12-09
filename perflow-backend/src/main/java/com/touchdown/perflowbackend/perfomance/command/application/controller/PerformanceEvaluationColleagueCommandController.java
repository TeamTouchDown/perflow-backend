package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.EvalutionListDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.EvalutionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfomances/col/perfo")
@RequiredArgsConstructor
public class PerformanceEvaluationColleagueCommandController {

    private final EvalutionCommandService evalutionCommandService;

    // 동료 평가 생성
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createColPerfo(
            @PathVariable("empId") String empId,
            @RequestBody EvalutionListDTO evalutionListDTO) {

        evalutionCommandService.createPerfo(evalutionListDTO, empId);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_UPLOAD_SUCCESS);
    }

}
