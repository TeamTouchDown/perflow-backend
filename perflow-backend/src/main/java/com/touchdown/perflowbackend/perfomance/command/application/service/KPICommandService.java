package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KPICommandService {

    private final KpiCommandRepository kpiCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;

    // 개인 KPI 생성
    @Transactional
    public void createPersonalKpi(KPIDetailRequestDTO kpiRequestDTO, String empId) {

        // empId를 통해 사원 정보 가져오기
        Employee emp = findEmployeeByEmpId(empId);

        // 받아온 내용과 사원 정보를 신규 개인 KPI에 입력하기
        Kpi kpi = PerformanceMapper.kpiDTOtoEntity(kpiRequestDTO, emp, PersonalType.PERSONAL);

        // 완성된 KPI DB에 저장하기
        kpiCommandRepository.save(kpi);
    }

    // 개인 KPI 수정
    @Transactional
    public void updatePersonalKpi(KPIDetailRequestDTO kpiRequestDTO, Long kpiId) {

        // 받아온 KPI id를 통해 기존 KPI 정보 받아오기
        Kpi kpi = findKpiByKpiId(kpiId);

        // 기존 KPI 정보에 받아온 수정사항 반영하기/
        kpi.updateKpi(kpiRequestDTO);

        // 반영된 KPI 저장하기
        kpiCommandRepository.save(kpi);

    }

    // 개인 KPI 삭제
    @Transactional
    public void deletePersonalKpi(Long kpiId, String empId){

        // 받아온 KPI id를 통해 기존 KPI 정보 받아오기
        Kpi kpi = findKpiByKpiId(kpiId);

        // 받아온 현재 유저 아이디와 작성자 아이디가 일치하는지 확인하기
        if (isSameWriter(kpi.getEmp().getEmpId() ,empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        // KPI 삭제하기
        kpiCommandRepository.deleteById(kpiId);

    }

    // KPI 작성자와 현재 유저가 일치하는지 확인
    private boolean isSameWriter(String kpiempId, String requestEmpId) {
        return !kpiempId.equals(requestEmpId);
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
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
