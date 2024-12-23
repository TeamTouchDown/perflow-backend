package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAnnualQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAnnualQueryService {

    private final WorkAttitudeAnnualQueryRepository annualRepository;
    private final EmployeeQueryRepository employeeRepository;

    // 직원: 연차 사용 횟수 조회 (종류별)
    public Object getAnnualUsage() {

        String empId = EmployeeUtil.getEmpId();

        return annualRepository.findAnnualUsageByEmpId(empId);
    }

    // 직원: 연차 잔여 횟수 조회 (반차 포함)
    public double getAnnualBalance() {

        String empId = EmployeeUtil.getEmpId();

        // 입사일 기준 연차 계산 로직
        int baseDays = 15; // 기본 연차 15일
        int yearsWorked = LocalDate.now().getYear() - employeeRepository.findHireYearByEmpId(empId);
        int totalAnnualDays = baseDays + (yearsWorked / 3); // 3년마다 1일 추가

        Double usedAnnualDays = Optional.ofNullable(annualRepository.countUsedAnnualDays(empId)).orElse(0.0);
        return totalAnnualDays - usedAnnualDays;
    }

    // 직원: 연차 전체 조회
    public List<WorkAttitudeAnnualResponseDTO> getAnnualList() {

        String empId = EmployeeUtil.getEmpId();
        return annualRepository.findAnnualListByEmpId(empId);
    }

    // 팀장: 부서 내 연차 내역 조회
    public List<WorkAttitudeAnnualResponseDTO> getTeamAnnualList() {
        String loggedInEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByEmpId(loggedInEmpId); // 팀장의 부서 ID 조회

        return annualRepository.findAnnualListByDeptId(String.valueOf(deptId));
    }

    // 인사팀: 모든 사원의 연차 내역 조회
    public List<WorkAttitudeAnnualResponseDTO> getAllAnnualList() {
        return annualRepository.findAllAnnualList();
    }
}
