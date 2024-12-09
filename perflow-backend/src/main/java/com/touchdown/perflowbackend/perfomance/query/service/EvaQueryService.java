package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.EvaQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaQueryService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final EvaQueryRepository evaQueryRepository;

    // 동료 평가 리스트 조회
    @Transactional(readOnly = true)
    public List<EvaDetailResponseDTO> getEvaColList(String empId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 현재 유저의 부서 체크하기
        Department dep = findDepartmentByDeptId(emp.getDept().getDepartmentId());

        // 현재 유저의 동료 리스트 저장
        List<EvaDetailResponseDTO> evaList = evaQueryRepository.findEmployeeDetailsWithExistence(dep.getDepartmentId(), empId, java.time.Year.now().getValue()) ;

        return evaList;
    }
    
    public List<EvaAnswerResponseDTO> getEvaColAnswer(String perfoempId, String perfoedempId) {

        // 유저가 존재하는지 체크하기
        Employee perfoemp = findEmployeeByEmpId(perfoempId);

        // 유저가 존재하는지 체크하기
        Employee perfoedemp = findEmployeeByEmpId(perfoedempId);

        // 현재 지정된 동료 평가의 답변 불러오기
        List<EvaAnswerResponseDTO> evaAnswerList = evaQueryRepository.findAnswerByEmpIds(perfoempId, perfoedempId, java.time.Year.now().getValue());

        return evaAnswerList;
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    private Department findDepartmentByDeptId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }
}
