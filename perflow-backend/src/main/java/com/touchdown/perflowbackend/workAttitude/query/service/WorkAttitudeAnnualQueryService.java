package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAnnualQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAnnualQueryService {

    private final WorkAttitudeAnnualQueryRepository annualRepository;
    private final EmployeeQueryRepository employeeRepository;

    // 연차 잔여 횟수 조회
    @Transactional
    public int getAnnualBalance() {
        String empId = EmployeeUtil.getEmpId();
        int providedAnnual = calculateProvidedAnnualCount(empId);
        int usedAnnual = calculateUsedAnnualCount(empId);
        return providedAnnual - usedAnnual;
    }

    // 직원 연차 전체 조회
    @Transactional
    public List<WorkAttitudeAnnualResponseDTO> getAnnualList() {
        String empId = EmployeeUtil.getEmpId();
        return annualRepository.findByEmpId(empId);
    }

    // 팀장 부서 연차 내역 조회
    @Transactional
    public List<WorkAttitudeAnnualResponseDTO> getTeamAnnualList() {
        Long deptId = Optional.ofNullable(employeeRepository.findDeptIdByEmpId(EmployeeUtil.getEmpId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
        return annualRepository.findByDepartment(deptId);
    }



    // 인사팀 전체 연차 내역 조회
    @Transactional
    public List<WorkAttitudeAnnualResponseDTO> getAllAnnualList() {
        return annualRepository.findAllAnnuals();
    }

    // 연차 사용 내역 조회 (종류별 사용량 포함)
    @Transactional
    public Map<String, Object> getAnnualUsageDetails() {
        String empId = EmployeeUtil.getEmpId();
        List<WorkAttitudeAnnualResponseDTO> usageList = annualRepository.findByEmpId(empId);

        Map<String, Long> usageCount = usageList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getAnnualType().toString(), Collectors.counting()));

        return Map.of(
                "empId", empId,
                "usageDetails", usageCount
        );
    }

    // 제공된 연차 개수 계산
    private int calculateProvidedAnnualCount(String empId) {
        LocalDate joinDate = employeeRepository.findJoinDateByEmpId(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
        int baseAnnual = 15;
        int maxAnnual = 25;
        int years = LocalDate.now().getYear() - joinDate.getYear();
        int additionalAnnual = (years / 3);
        return Math.min(baseAnnual + additionalAnnual, maxAnnual);
    }

    // 사용한 연차 개수 계산
    private int calculateUsedAnnualCount(String empId) {
        return annualRepository.countUsedAnnuals(empId);
    }


    // 연차 사용 내역 조회 (종류별 사용량 포함)
    @Transactional
    public Map<String, Long> getAnnualUsage() {
        String empId = EmployeeUtil.getEmpId();
        List<WorkAttitudeAnnualResponseDTO> usageList = annualRepository.findByEmpId(empId);

        return usageList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getAnnualType().toString(), Collectors.counting()));
    }

}
