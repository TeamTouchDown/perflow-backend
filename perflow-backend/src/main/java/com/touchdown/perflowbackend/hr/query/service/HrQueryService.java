package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponse;
import com.touchdown.perflowbackend.hr.query.repository.DepartmentQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrQueryService {

    private final DepartmentQueryRepository departmentQueryRepository;

    public List<DepartmentQueryResponse> readAllDepartments() {

        List<Department> allDepartments = findAllDepartments();

        List<Department> topLevelDepartments = findTopLevelDepartments(allDepartments);

        return topLevelDepartments.stream()
                .map(department -> buildDepartmentTree(department, allDepartments))
                .collect(Collectors.toList());
    }

    // 모든 부서 탐색
    private List<Department> findAllDepartments() {

        return departmentQueryRepository.findAllDepts()
                .orElse(Collections.emptyList());
    }

    // 상위 부서가 없는 최상위 부서 탐색
    private List<Department> findTopLevelDepartments(List<Department> allDepartments) {

        return allDepartments.stream()
                .filter(department -> department.getManageDept() == null)
                .toList();
    }

    // 부서 트리 구조 만들기
    private DepartmentQueryResponse buildDepartmentTree(Department department, List<Department> allDepartments) {

        // 현재 부서의 자식 부서 탐색
        List<DepartmentQueryResponse> subDepartments = allDepartments.stream()
                .filter(subDept -> department.equals(subDept.getManageDept()))
                .map(subDept -> buildDepartmentTree(subDept, allDepartments))
                .collect(Collectors.toList());

        return new DepartmentQueryResponse(department.getDepartmentId(), department.getName(), subDepartments);
    }


}
