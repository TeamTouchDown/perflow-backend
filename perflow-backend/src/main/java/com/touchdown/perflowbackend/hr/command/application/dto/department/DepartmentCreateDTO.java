package com.touchdown.perflowbackend.hr.command.application.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateDTO {

    private Long departmentId;

    private String name;

    private String responsibility;

    private String contact;

    private Long manageDeptId;

    private String picId;
}
