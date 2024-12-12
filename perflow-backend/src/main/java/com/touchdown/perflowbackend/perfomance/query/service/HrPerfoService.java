package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.HrPerfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrPerfoService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final HrPerfoQueryRepository hrPerfoQueryRepository;

    // 인사 평가 조회
    @Transactional(readOnly = true)
    public List<HrPerfoResponseDTO> getHrPerfo(String empId){

        // 사원이 존재하는지 조회하기
        Employee emp = findEmployeeByEmpId(empId);

        // empId를 통해 인사 평가 가져오기
        List<HrPerfoResponseDTO> hrPerfoList = hrPerfoQueryRepository.findHrPerfoByEmpId(empId);

        return hrPerfoList;
    }

    // 사번으로 사원 정보 찾기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
