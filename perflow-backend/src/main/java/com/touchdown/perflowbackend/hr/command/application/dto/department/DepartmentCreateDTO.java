package com.touchdown.perflowbackend.hr.command.application.dto.department;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentCreateDTO {

    private Long departmentId;

    private String name;

    private String responsibility;

    private String contact;

    private Long manageDeptId;

    private String picId;
}
