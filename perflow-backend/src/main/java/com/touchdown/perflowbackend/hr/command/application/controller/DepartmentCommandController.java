package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.service.DepartmentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/hr/departments")
public class DepartmentCommandController {

    private final DepartmentCommandService departmentCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> createDepartment(
            @RequestBody DepartmentCreateDTO departmentCreateDTO
    ) {
        departmentCommandService.createDepartment(departmentCreateDTO);

        return ResponseEntity.ok(SuccessCode.DEPARTMENT_CREATE_SUCCESS);
    }

    @PutMapping("/{deptId}")
    public ResponseEntity<SuccessCode> updateDepartment(
            @RequestBody DepartmentUpdateDTO departmentUpdateDTO,
            @PathVariable(name = "deptId") Long deptId
    ) {
        departmentCommandService.updateDepartment(departmentUpdateDTO, deptId);

        return ResponseEntity.ok(SuccessCode.DEPARTMENT_UPDATE_SUCCESS);
    }
}
