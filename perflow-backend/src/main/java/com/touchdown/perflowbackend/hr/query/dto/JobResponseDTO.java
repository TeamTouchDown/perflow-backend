package com.touchdown.perflowbackend.hr.query.dto;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobResponseDTO {

    private Long jobId;

    private Long deptId;

    private String deptName;

    private String name;

    private String responsibility;

    private Status status;

    @Builder
    public JobResponseDTO(Job job) {

        this.jobId = job.getJobId();
        this.deptId = job.getDept().getDepartmentId();
        this.deptName = job.getDept().getName();
        this.name = job.getName();
        this.responsibility = job.getResponsibility();
        this.status = job.getStatus();
    }
}
