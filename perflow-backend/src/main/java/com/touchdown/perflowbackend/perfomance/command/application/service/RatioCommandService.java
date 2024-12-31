package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreateGradeRatioRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreatePerfoRatioRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.GradeRatio;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Weight;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.GradeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.WeightCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatioCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final WeightCommandRepository weightCommandRepository;
    private final GradeCommandRepository gradeCommandRepository;

    // 인사 평가 반영 비율 생성
    @Transactional
    public void createPerfoRatio(String empId, Long deptId, CreatePerfoRatioRequestDTO createPerfoRatioRequestDTO) {

        // 작성자 정보 확인
        Employee emp = findEmployeeByEmpId(empId);

        // 부서 정보 확인
        Department dept = findDepartmentByDeptId(deptId);

        // 인사 평가 반영 비율 생성
        Weight weight = PerformanceMapper.perforatioDTOtoWeight(emp,dept,createPerfoRatioRequestDTO);

        // 비율 저장
        weightCommandRepository.save(weight);
    }

    // 등급 비율 생성
    @Transactional
    public void createGradeRatio(String empId, CreateGradeRatioRequestDTO createGradeRatioRequestDTO) {

        // 작성자 정보 확인
        Employee emp = findEmployeeByEmpId(empId);

        // 등급 비율 생성
        GradeRatio gradeRatio = PerformanceMapper.GraderatioDTOtoGradeRatio(emp,createGradeRatioRequestDTO);

        gradeCommandRepository.save(gradeRatio);
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 받아온 DEPT id를 이용해 DEP 정보 불러오기
    private Department findDepartmentByDeptId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }
}
