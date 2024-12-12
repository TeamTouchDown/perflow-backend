package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class JobMapper {

    public static Job toEntity(JobCreateDTO jobCreateDTO, Department department) {

        return Job.builder()
                .createDTO(jobCreateDTO)
                .dept(department)
                .build();
    }

    public static List<JobResponseDTO> toJobResponseDTOList(List<Job> jobs) {

        List<JobResponseDTO> jobResponseDTOList = new ArrayList<>();

        for (Job job : jobs) {
            jobResponseDTOList.add(
                    JobResponseDTO.builder()
                            .job(job)
                            .build()
            );
        }

        return jobResponseDTOList;
    }
}
