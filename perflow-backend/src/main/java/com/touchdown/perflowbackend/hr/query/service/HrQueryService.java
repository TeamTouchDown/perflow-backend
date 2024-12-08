package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.Mapper.HrMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponseDTO;
import com.touchdown.perflowbackend.hr.query.repository.DepartmentQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrQueryService {

    private final DepartmentQueryRepository departmentQueryRepository;

    @Transactional(readOnly = true)
    public List<DepartmentQueryResponseDTO> readAllDepartments() {

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

    // 최상위 부서 탐색
    private List<Department> findTopLevelDepartments(List<Department> allDepartments) {

        return allDepartments.stream()
                .filter(department -> department.getManageDept() == null)
                .toList();
    }

    // 부서 트리 구조 만들기
    private DepartmentQueryResponseDTO buildDepartmentTree(Department department, List<Department> allDepartments) {

        // 현재 부서의 자식 부서 탐색
        List<DepartmentQueryResponseDTO> subDepartments = allDepartments.stream()
                .filter(subDept -> department.equals(subDept.getManageDept()))
                .map(subDept -> buildDepartmentTree(subDept, allDepartments))
                .collect(Collectors.toList());

        return new DepartmentQueryResponseDTO(department.getDepartmentId(), department.getName(), subDepartments);
    }


    @Transactional(readOnly = true)
    public List<DepartmentQueryResponseDTO> searchDepartmentsByName(String name) {

        if(name == null || name.isEmpty()) {
            log.info("검색 사용 안 한 상태 -> 전체 조직도 보여주기");
            return readAllDepartments();
        }

        List<Department> departments = findDepartmentByName(name);

        return departments.stream()
                .map(this::buildTopDeptTree)
                .collect(Collectors.toList());
    }

    // 검색한 부서의 최상위 부서 탐색
    private DepartmentQueryResponseDTO buildTopDeptTree(Department department) {

        Department topDept = findTopLevelDepartment(department);

        return buildDepartmentTree(topDept, findAllDepartments());
    }

    // 최상위 부서 탐색
    private Department findTopLevelDepartment(Department department) {

        if(department.getManageDept() == null) {
            return department;
        }

        return findTopLevelDepartment(department.getManageDept());
    }

    private List<Department> findDepartmentByName(String name) {

        return departmentQueryRepository.findByNameContaining(name)
                .orElse(Collections.emptyList());
    }
}
