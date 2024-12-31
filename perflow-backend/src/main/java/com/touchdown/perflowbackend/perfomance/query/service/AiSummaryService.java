package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import com.touchdown.perflowbackend.perfomance.query.dto.AiSummaryResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.AiPerfoSummaryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiSummaryService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final AiPerfoSummaryQueryRepository aiPerfoSummaryQueryRepository;

    // Ai 요약 불러오기
    public AiSummaryResponseDTO getAiSummary(String empId, String type){

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        AiSummaryResponseDTO aiSummaryResponseDTO = aiPerfoSummaryQueryRepository.
                findAiPerfoSummaryByempIdandtype(java.time.Year.now().getValue(), PerfoType.valueOf(type), empId);

        return aiSummaryResponseDTO;
    }

    // 사번으로 사원 정보 찾기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
