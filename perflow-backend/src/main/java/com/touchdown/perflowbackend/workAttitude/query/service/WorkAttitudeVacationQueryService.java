package com.touchdown.perflowbackend.workAttitude.query.service;


import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeVacationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkAttitudeVacationQueryService {

    private final EmployeeQueryRepository employeeRepository;
    private final WorkAttitudeVacationQueryRepository vacationQueryRepository;


    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getVacationUsage() {
        // 로그인 사용자 ID 조회
        String empId = EmployeeUtil.getEmpId();

        // 개인 휴가 사용 정보 조회
        return vacationQueryRepository.findVacationUsage(empId);
    }

    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getVacationDetails() {
        // 로그인 사용자 ID 조회
        String empId = EmployeeUtil.getEmpId();

        // 개인 휴가 내역 조회
        return vacationQueryRepository.findVacationDetails(empId);

    }

    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getTeamVacationList() {
        // 로그인 사용자 ID 조회
        String empId = EmployeeUtil.getEmpId();

        // 팀장의 부서 ID 조회
        Long deptId = employeeRepository.findDeptIdByEmpId(empId);

        // 팀원 휴가 내역 조회
        return vacationQueryRepository.findTeamVacationList(deptId);
    }

    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getAllVacationList() {
        // 인사팀은 모든 데이터 조회 (로그인 ID 불필요)
        return vacationQueryRepository.findAllVacationList();
    }
}
