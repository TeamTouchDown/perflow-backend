package com.touchdown.perflowbackend.perfomance.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreateQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.service.EvalutionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr/perfomances/col/perfo/question")
@RequiredArgsConstructor
public class PerformanceEvaluationColleagueQuestionCommandController {

    private final EvalutionCommandService evalutionCommandService;

    // 동료 평가 문항 생성
    @PostMapping("/{empId}")
    public ResponseEntity<SuccessCode> createColQuestion(
            @PathVariable("empId") String empId,
            @RequestBody CreateQuestionRequestDTO createQuestionRequestDTO ) {

        evalutionCommandService.createQuestion(empId, createQuestionRequestDTO);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_QUESTION_UPLOAD_SUCCESS);

    }

    // 동료 평가 문항 수정
    @PutMapping("/{empId}/{perfoQuestionId}")
    public ResponseEntity<SuccessCode> updateColQuestion(
            @PathVariable("empId") String empId,
            @PathVariable("perfoQuestionId") Long perfoQuestionId,
            @RequestBody UpdateQuestionRequestDTO updateQuestionRequestDTO) {

        evalutionCommandService.updateQuestion(empId, perfoQuestionId, updateQuestionRequestDTO);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_QUESTION_UPDATE_SUCCESS);
    }

    // 동료 평가 문항 삭제
    @DeleteMapping("/{empId}/{perfoQuestionId}")
    public ResponseEntity<SuccessCode> deleteColQuestion(
            @PathVariable("empId") String empId,
            @PathVariable("perfoQuestionId") Long perfoQuestionId) {

        evalutionCommandService.deleteQuestion(empId, perfoQuestionId);

        return ResponseEntity.ok(SuccessCode.EVALUTION_COL_QUESTION_DELETE_SUCCESS);
    }
}
