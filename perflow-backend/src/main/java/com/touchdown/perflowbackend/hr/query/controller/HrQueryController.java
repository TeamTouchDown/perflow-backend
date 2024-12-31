package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.DepartmentListResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponseDTO;
import com.touchdown.perflowbackend.hr.query.service.HrQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HrQueryController {

    private final HrQueryService hrQueryService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentQueryResponseDTO>> readDepartments() {

        List<DepartmentQueryResponseDTO> response = hrQueryService.readAllDepartments();
        return ResponseEntity.ok(response);
    }

    // 조직도에 사용될, 가공되지 않은 전체 부서 목록 조회
    @GetMapping("/departments/list")
    public ResponseEntity<List<DepartmentListResponseDTO>> readDepartmentList() {

        List<DepartmentListResponseDTO> response = hrQueryService.readDepartmentList();

        return ResponseEntity.ok(response);
    }

}
