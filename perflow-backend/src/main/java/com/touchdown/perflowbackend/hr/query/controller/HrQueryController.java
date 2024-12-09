package com.touchdown.perflowbackend.hr.query.controller;

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
@RequestMapping("/api/v1/hr")
public class HrQueryController {

    private final HrQueryService hrQueryService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentQueryResponseDTO>> readDepartments() {

        List<DepartmentQueryResponseDTO> response = hrQueryService.readAllDepartments();
        return ResponseEntity.ok(response);
    }

}
