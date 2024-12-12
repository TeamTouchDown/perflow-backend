package com.touchdown.perflowbackend.hr.command.application.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentUpdateDTO {

    private String name;

    private String responsibility;

    private String contact;

    private Long manageDeptId;

    private String picId;
}
