package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioGradeResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioPerfoResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.GradeRatioQueryRepository;
import com.touchdown.perflowbackend.perfomance.query.repository.WeightQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatioQueryService {

    private final DepartmentCommandRepository departmentCommandRepository;
    private final WeightQueryRepository weightQueryRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final GradeRatioQueryRepository gradeRatioQueryRepository;

    // 인사 평가 비율 조회
    @Transactional(readOnly = true)
    public RatioPerfoResponseDTO getPerfoWeight(Long deptId){

        // 부서가 존재하는지 체크하기
        Department dep = findDepartmentByDeptId(deptId);

        // 인사 평가 비율 정보 불러오기
        RatioPerfoResponseDTO weight = weightQueryRepository.findWeightByDeptId(deptId);

        return weight;
    }

    // 등급 비율 조회
    @Transactional(readOnly = true)
    public RatioGradeResponseDTO getGradeRatio(String empId){

        // 회원 정보 존재여부 확인
        Employee emp = findEmployeeByEmpId(empId);

        // 등급 비율 정보 불러오기
        RatioGradeResponseDTO ratiograde = gradeRatioQueryRepository.findGradeRatio();

        return ratiograde;
    }

    // 부서번호로 부서 정보 찾기
    private Department findDepartmentByDeptId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
