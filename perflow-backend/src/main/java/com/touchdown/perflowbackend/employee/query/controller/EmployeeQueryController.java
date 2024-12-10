package com.touchdown.perflowbackend.employee.query.controller;

import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/employees")
public class EmployeeQueryController {

    private final EmployeeQueryService employeeQueryService;

    // 검색한 부서에 속한 회원 조회
    @GetMapping
    public ResponseEntity<List<EmployeeQueryResponseDTO>> readEmployees(@RequestParam(name = "departmentId") Long departmentId) {

        return ResponseEntity.ok(employeeQueryService.readAllEmployees(departmentId));
    }

}
