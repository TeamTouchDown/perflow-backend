package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;
import com.touchdown.perflowbackend.hr.query.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobQueryController {

    final JobQueryService jobQueryService;

    @GetMapping("/job")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {

        List<JobResponseDTO> jobs = jobQueryService.getAllJobs();

        return ResponseEntity.ok(jobs);
    }
}
