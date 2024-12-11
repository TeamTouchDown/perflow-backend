package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.Mapper.DepartmentMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentListResponseDTO;
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

    // 트리 구조로 만들지 않은 모든 부서 데이터 조회
    public List<DepartmentListResponseDTO> readDepartmentList() {

        List<Department> allDepartmentEntity = findAllDepartments();

        return DepartmentMapper.toListDTO(allDepartmentEntity);
    }
}
