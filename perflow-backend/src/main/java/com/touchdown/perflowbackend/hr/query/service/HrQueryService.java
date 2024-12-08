package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponse;
import com.touchdown.perflowbackend.hr.query.repository.DepartmentQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrQueryService {

    private final DepartmentQueryRepository departmentQueryRepository;

    public List<DepartmentQueryResponse> readAllDepartments() {

        List<Department> allDepartments = departmentQueryRepository.findAllDepartments();

        // 상위 부서가 없는 최상위 부서 조회
        List<Department> topLevelDepartments = allDepartments.stream()
                .filter(department -> department.getManageDept() == null)
                .toList();

        return topLevelDepartments.stream()
                .map(department -> buildDepartmentTree(department, allDepartments))
                .collect(Collectors.toList());
    }
    
    private DepartmentQueryResponse buildDepartmentTree(Department department, List<Department> allDepartments) {

        // 현재 부서의 자식 부서들
        List<DepartmentQueryResponse> subDepartments = allDepartments.stream()
                .filter(subDept -> department.equals(subDept.getManageDept()))
                .map(subDept -> buildDepartmentTree(subDept, allDepartments))
                .collect(Collectors.toList());

        // 현재 부서와 자식 부서를 응답 DTO로 변환
        return new DepartmentQueryResponse(department.getDepartmentId(), department.getName(), subDepartments);
    }


}
