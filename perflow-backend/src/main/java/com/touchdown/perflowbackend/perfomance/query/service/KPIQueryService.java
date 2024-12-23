package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.service.KPICommandService;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.*;
import com.touchdown.perflowbackend.perfomance.query.repository.KPIQueryRepository;
import com.touchdown.perflowbackend.perfomance.query.repository.KPIStatusQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper.kpiListToDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KPIQueryService {

    private final KPIQueryRepository kpiQueryRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final KpiCommandRepository kpiCommandRepository;
    private final KPIStatusQueryRepository kpiStatusQueryRepository;

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

    // 개인 KPI 조건부 리스트 조회
    @Transactional(readOnly = true)
    public KPIListResponseDTO getPersonalKPIsByYear(String empId, KPIPeriodRequestDTO kpiPeriodRequestDTO) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 해당 유저의 개인 KPI 가져오기
        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findPersonalKPIsByEmpIdAndYear(empId ,kpiPeriodRequestDTO);

        // 해당 유저의 부서의 KPI 제한치 가져오기
        KPILimitResponseDTO limit = kpiQueryRepository.findPersonalKPILimitByEmpId(empId)
                .orElse(new KPILimitResponseDTO(2L, 5L)); // 기본값 설정


        // 가져온 개인 KPI를 리스트에 넣어 처리하기
        return kpiListToDTO(lists,limit);
    }

    // 팀 KPI 리스트 조회
    @Transactional(readOnly = true)
    public KPIListResponseDTO getTeamKPIs(String empId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 해당 유저의 팀 KPI 가져오기
        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findTeamKPIsByEmpId(empId);

        // 해당 유저의 부서의 KPI 제한치 가져오기
        KPILimitResponseDTO limit = kpiQueryRepository.findTeamKPILimitByEmpId(empId)
                .orElse(new KPILimitResponseDTO(2L, 5L)); // 기본값 설정


        // 가져온 팀 KPI를 리스트에 넣어 처리하기
        return kpiListToDTO(lists,limit);
    }

    // 개인 KPI 조건부 리스트 조회
    @Transactional(readOnly = true)
    public KPIListResponseDTO getTeamKPIsByYear(String empId, KPIPeriodRequestDTO kpiPeriodRequestDTO) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 해당 유저의 개인 KPI 가져오기
        List<KPIDetailResponseDTO> lists = kpiQueryRepository.findTeamKPIsByEmpIdAndYear(empId ,kpiPeriodRequestDTO);

        // 해당 유저의 부서의 KPI 제한치 가져오기
        KPILimitResponseDTO limit = kpiQueryRepository.findTeamKPILimitByEmpId(empId)
                .orElse(new KPILimitResponseDTO(2L, 5L)); // 기본값 설정


        // 가져온 개인 KPI를 리스트에 넣어 처리하기
        return kpiListToDTO(lists,limit);
    }

    // KPI 반려 사유 조회
    @Transactional(readOnly = true)
    public KPIRejectReponseDTO getKPIRejectResponse(String empId, Long kpiId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 받아온 KPI id를 통해 기존 KPI 정보 받아오기
        Kpi kpi = findKpiByKpiId(kpiId);

        KPIRejectReponseDTO rejectreason = kpiStatusQueryRepository.findrejectbyempIdandkpiId(empId, kpiId);

        return rejectreason;
    }

    // 받아온 EMP Id를 이용해 사원 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 받아온 KPI id를 이용해 KPI 정보 불러오기
    private Kpi findKpiByKpiId(Long kpiId) {
        return kpiCommandRepository.findByKpiId(kpiId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_KPI));
    }
}
