package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.HrPerfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfomances/hrperfo")
@RequiredArgsConstructor
public class PerfomanceHrPerfoQueryController {

    private final HrPerfoService hrPerfoService;

    // 인사 평가 조회
    @GetMapping("/{empId}")
    public ResponseEntity<List<HrPerfoResponseDTO>> getHrPerfo(
            @PathVariable(name = "empId") String empId) {

        List<HrPerfoResponseDTO> response = hrPerfoService.getHrPerfo(empId);

        return ResponseEntity.ok(response);
    }
}
