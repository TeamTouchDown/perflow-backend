package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
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


    @Transactional
    public void createPersonalKpi(KPIDetailRequestDTO kpiRequestDTO, String empId) {

        Employee emp = findEmployeeByEmpId(empId);


        Kpi kpi = PerformanceMapper.kpiDTOtoEntity(kpiRequestDTO, emp);

        kpiCommandRepository.save(kpi);

    }

    @Transactional
    public void updatePersonalKpi(KPIDetailRequestDTO kpiRequestDTO, Long kpiId) {

        Kpi kpi = findKpiByKpiId(kpiId);

        kpi.updateKpi(kpiRequestDTO);

        kpiCommandRepository.save(kpi);

    }

    @Transactional
    public void deletePersonalKpi(Long kpiId, String empId){
        Kpi kpi = findKpiByKpiId(kpiId);

        if (isSameWriter(kpi.getEmp().getEmpId() ,empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        kpiCommandRepository.deleteById(kpiId);

    }

    private Employee findEmployeeByEmpId(String empId) {

        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    private Kpi findKpiByKpiId(Long kpiId) {
        return kpiCommandRepository.findByKpiId(kpiId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    private boolean isSameWriter(String kpiempId, String requestEmpId) {

        System.out.println("kpiempId: " + kpiempId);
        System.out.println("requestEmpId: " + requestEmpId);

        return !kpiempId.equals(requestEmpId);
    }


}
