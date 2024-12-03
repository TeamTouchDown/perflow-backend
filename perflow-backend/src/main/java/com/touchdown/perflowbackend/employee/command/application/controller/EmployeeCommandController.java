package com.touchdown.perflowbackend.employee.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/employees")
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;

    @PostMapping
    public ResponseEntity<String> registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {

        employeeCommandService.registerEmployee(employeeRegisterDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }
}
