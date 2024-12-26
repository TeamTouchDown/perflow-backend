package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseListDTO;
import com.touchdown.perflowbackend.hr.query.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
public class JobQueryController {

    final JobQueryService jobQueryService;

    @GetMapping("/job")
    public ResponseEntity<JobResponseListDTO> getAllJobs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        JobResponseListDTO jobs = jobQueryService.getAllJobs(pageable);

        return ResponseEntity.ok(jobs);
    }
}
