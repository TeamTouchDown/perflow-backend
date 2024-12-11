package com.touchdown.perflowbackend.employee.query.controller;

import com.touchdown.perflowbackend.employee.query.dto.EmployeeDetailResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.service.EmployeeQueryService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeQueryController {

    private final EmployeeQueryService employeeQueryService;

    // 검색한 부서에 속한 회원 조회
    @GetMapping("/employees/depts")
    public ResponseEntity<List<EmployeeQueryResponseDTO>> readEmployees(@RequestParam(name = "departmentId") Long departmentId) {

        return ResponseEntity.ok(employeeQueryService.readDeptEmployees(departmentId));
    }

    // 모든 사원 목록 조회
    @GetMapping("/employees/lists")
    public ResponseEntity<List<EmployeeQueryResponseDTO>> readEmployees() {

        return ResponseEntity.ok(employeeQueryService.readAllEmployees());
    }

    @GetMapping("/hr/employees/detail/{empId}") // 관리자의 사원 정보 조회
    public ResponseEntity<EmployeeDetailResponseDTO> readEmployeeDetail(
            @PathVariable(value = "empId") String empId
    ) {

        return ResponseEntity.ok(employeeQueryService.readEmployeeDetail(empId));
    }

    @GetMapping("/employees/detail") // 내 상세정보 조회
    public ResponseEntity<EmployeeDetailResponseDTO> readMyDetail() {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(employeeQueryService.readEmployeeDetail(empId));
    }
}
