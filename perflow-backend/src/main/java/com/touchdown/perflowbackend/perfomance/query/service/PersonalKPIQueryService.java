package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.KPIQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper.kpiListToDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalKPIQueryService {

    private final KPIQueryRepository kpiQueryRepository;
    private final EmployeeCommandRepository employeeCommandRepository;

    // 개인 KPI 리스트 조회
    @Transactional(readOnly = true)
    public KPIListResponseDTO getPersonalKPIs(String empId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 해당 유저의 개인 KPI 가져오기
        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findPersonalKPIsByEmpId(empId);

        // 해당 유저의 부서의 KPI 제한치 가져오기
        KPILimitResponseDTO limit = kpiQueryRepository.findPersonalKPILimitByEmpId(empId)
                .orElse(new KPILimitResponseDTO(2L, 5L)); // 기본값 설정


        // 가져온 개인 KPI를 리스트에 넣어 처리하기
        return kpiListToDTO(lists,limit);
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
