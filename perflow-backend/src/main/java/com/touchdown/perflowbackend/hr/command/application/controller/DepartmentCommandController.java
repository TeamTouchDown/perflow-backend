package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.service.DepartmentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DepartmentCommandController {

    private final DepartmentCommandService departmentCommandService;

    @PostMapping("/hr/departments")
    public ResponseEntity<SuccessCode> createDepartment(
            @RequestBody DepartmentCreateDTO departmentCreateDTO
    ) {
        departmentCommandService.createDepartment(departmentCreateDTO);

        return ResponseEntity.ok(SuccessCode.DEPARTMENT_CREATE_SUCCESS);
    }
}
