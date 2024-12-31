package com.touchdown.perflowbackend.perfomance.query.controller;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoInquiry;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoInquiryResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.service.HrPerfoInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/perfomances/inquiry")
@RequiredArgsConstructor
public class PerfomanceHrPerfoInquiryQueryController {

    private final HrPerfoInquiryService hrPerfoInquiryservice;

    // 인사 평가 의의제기 리스트 조회
    @GetMapping("/{deptId}")
    public ResponseEntity<List<HrPerfoInquiryResponseDTO>> getHrPerfoInquiryList(
            @PathVariable(name = "deptId") Long deptId) {

        List<HrPerfoInquiryResponseDTO> response =  hrPerfoInquiryservice.getHrPerfoInquiry(deptId);

        return ResponseEntity.ok(response);
    }
}
