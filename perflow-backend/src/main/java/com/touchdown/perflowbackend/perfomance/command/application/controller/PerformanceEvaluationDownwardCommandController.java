package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.EvalutionListDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.EvalutionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leader/perfomances/downward/perfo")
@RequiredArgsConstructor
public class PerformanceEvaluationDownwardCommandController {

    private final EvalutionCommandService evalutionCommandService;

    // 하향 평가 작성
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createDownPerfo(
            @PathVariable("empId") String empId,
            @RequestBody EvalutionListDTO evalutionListDTO) {

        evalutionCommandService.createPerfo(evalutionListDTO, empId);

        return ResponseEntity.ok(SuccessCode.EVALUTION_DOWN_UPLOAD_SUCCESS);
    }

    // 하향 평가 수정
    @PutMapping("/{empId}")
    public ResponseEntity<SuccessCode> updateColPerfo(
            @PathVariable("empId") String empId,
            @RequestBody EvalutionListDTO evalutionListDTO ) {

        evalutionCommandService.updatePerfo(evalutionListDTO, empId);

        return ResponseEntity.ok(SuccessCode.EVALUTION_DOWN_UPDATE_SUCCESS);
    }
}
