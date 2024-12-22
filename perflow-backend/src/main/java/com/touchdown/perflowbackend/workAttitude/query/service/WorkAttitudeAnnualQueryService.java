package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAnnualQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAnnualQueryService {

    private final WorkAttitudeAnnualQueryRepository annualRepository;
    private final EmployeeQueryRepository employeeRepository;

    // 직원: 연차 사용 횟수 조회 (종류별)
    public Object getAnnualUsage(String empId) {
        // 예제 로직 - 사용 횟수 계산 (추후 수정 필요)
        return annualRepository.findAnnualUsageByEmpId(empId);
    }

    // 직원: 연차 잔여 횟수 조회
    public Object getAnnualBalance(String empId) {
        // 입사일 기준 연차 계산 로직
        int baseDays = 15;
        int yearsWorked = LocalDate.now().getYear() - employeeRepository.findHireYearByEmpId(empId);
        int totalAnnualDays = baseDays + (yearsWorked / 3);

        int usedAnnualDays = annualRepository.countUsedAnnualDays(empId);
        return totalAnnualDays - usedAnnualDays;
    }

    // 직원: 연차 전체 조회
    public List<WorkAttitudeAnnualResponseDTO> getAnnualList(String empId) {
        return annualRepository.findAnnualListByEmpId(empId);
    }

    // 팀장: 부서 내 연차 내역 조회
    public List<WorkAttitudeAnnualResponseDTO> getTeamAnnualList(String deptId) {
        return annualRepository.findAnnualListByDeptId(deptId);
    }

    // 인사팀: 모든 사원의 연차 내역 조회
    public List<WorkAttitudeAnnualResponseDTO> getAllAnnualList() {
        return annualRepository.findAllAnnualList();
    }
}
