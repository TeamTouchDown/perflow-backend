package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.service.JobCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobCommandController {

    private final JobCommandService jobCommandService;

    @PostMapping("/hr/job")
    public ResponseEntity<SuccessCode> createJob(
            @RequestBody JobCreateDTO jobCreateDTO
    ) {
        jobCommandService.createJob(jobCreateDTO);

        return ResponseEntity.ok(SuccessCode.JOB_CREATE_SUCCESS);
    }

    @PutMapping("/hr/job")
    public ResponseEntity<SuccessCode> updateJob(
            @RequestBody JobUpdateDTO jobUpdateDTO
    ) {

        jobCommandService.updateJob(jobUpdateDTO);

        return ResponseEntity.ok(SuccessCode.JOB_UPDATE_SUCCESS);
    }
}
