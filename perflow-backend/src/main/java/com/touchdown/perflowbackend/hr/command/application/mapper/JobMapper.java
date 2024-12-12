package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;

public class JobMapper {

    public static Job toEntity(JobCreateDTO jobCreateDTO, Department department) {

        return Job.builder()
                .createDTO(jobCreateDTO)
                .dept(department)
                .build();
    }
}
