package com.touchdown.perflowbackend.employee.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginResponseDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeePwdRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/employees")
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> registerEmployee(
            @RequestBody EmployeeRegisterDTO employeeRegisterDTO) {

        employeeCommandService.registerEmployee(employeeRegisterDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @GetMapping("/login")
    public ResponseEntity<SuccessCode> loginRequestEmployee(@RequestBody EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        EmployeeLoginResponseDTO responseDTO = employeeCommandService.loginRequestEmployee(employeeLoginRequestDTO);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + responseDTO.getAccessToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.LOGIN_SUCCESS);
    }

    @PutMapping("/pwd")
    public ResponseEntity<SuccessCode> registerEmployeePassword(@RequestBody EmployeePwdRegisterDTO employeePwdRegisterDTO) {

        employeeCommandService.registerEmployeePassword(employeePwdRegisterDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
