package com.touchdown.perflowbackend.hr.command.application.dto.job;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobCreateDTO {

    private Long deptId;

    private String name;

    private String responsibility;

    private Status status;

}
