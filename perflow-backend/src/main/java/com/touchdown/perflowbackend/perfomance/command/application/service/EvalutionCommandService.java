package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.EvalutionListDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvalutionCommandService {

    private final PerfoCommandRepository perfoCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final PerfoQuestionCommandRepository perfoQuestionCommandRepository;

    // 동료 평가 생성
    @Transactional
    public void createPerfo(EvalutionListDTO evalutionListDTO, String empId) {

        // 평가자 정보 받아오기
        Employee perfoEmp = findEmployeeByEmpId(empId);

        // 피평가자 정보 받아오기
        Employee perfoedEmp = findEmployeeByEmpId(evalutionListDTO.getPerfoedEmpId());

        // 평가자와 피평가자의 부서가 일치하는지 확인
        if (isSameDepartment(perfoEmp.getDept().getDepartmentId() ,perfoedEmp.getDept().getDepartmentId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_DEPARTMENT);
        }

        // 평가 엔터티에 받아온 내용 매핑하기
        List<Perfo> perfos  = PerformanceMapper.evaluationAnswertoPerfo(evalutionListDTO, perfoEmp, perfoedEmp, perfoQuestionCommandRepository);

        perfoCommandRepository.saveAll(perfos);
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 평가자와 피평가자의 부서가 일치하는지 확인
    private boolean isSameDepartment(Long perfoempDepartment, Long perfoedempDepartment) {
        return !perfoempDepartment.equals(perfoedempDepartment);
    }
}
