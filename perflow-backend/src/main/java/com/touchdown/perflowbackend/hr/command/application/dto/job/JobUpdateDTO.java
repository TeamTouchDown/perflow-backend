package com.touchdown.perflowbackend.hr.command.application.dto.job;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobUpdateDTO {

    private Long jobId;

    private Long deptId;

    private String name;

    private String responsibility;
}
