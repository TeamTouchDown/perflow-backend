package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoHistoryResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.HrPerfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/perfomances/perfo/adjustment")
@RequiredArgsConstructor
public class PerfomanceHrPerfoHistoryQueryController {

    private final HrPerfoService hrPerfoService;

    @GetMapping("/{empId}")
    public ResponseEntity<List<HrPerfoHistoryResponseDTO>> findHrPerfoHistoryByEmpId(
            @PathVariable("empId") String empId) {

        List<HrPerfoHistoryResponseDTO> response = hrPerfoService.findHrPerfoHistoryByEmpId(empId);

        return ResponseEntity.ok(response);


    }
}
