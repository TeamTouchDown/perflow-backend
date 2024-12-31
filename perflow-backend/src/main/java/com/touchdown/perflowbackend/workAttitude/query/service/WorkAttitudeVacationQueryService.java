package com.touchdown.perflowbackend.workAttitude.query.service;


import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeVacationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkAttitudeVacationQueryService {

    private final EmployeeQueryRepository employeeRepository;
    private final WorkAttitudeVacationQueryRepository vacationQueryRepository;

    // 휴가 사용 내역 조회 (종류별 사용량 포함)
    @Transactional
    public Map<String, Object> getVacationUsageDetails() {

        String empId = EmployeeUtil.getEmpId();
        List<WorkAttitudeVacationResponseDTO> usageList = vacationQueryRepository.findByEmpId(empId);

        Map<String, Long> usageCount = usageList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getVacationType().toString(), Collectors.counting()));

        return Map.of(
                "empId", empId,
                "usageDetails", usageCount
        );
    }

    // 휴가 상세 조회
    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getVacationDetails() {

        String empId = EmployeeUtil.getEmpId();
        return vacationQueryRepository.findDetailsByEmpId(empId);
    }

    // 가장 가까운 휴가 조회
    @Transactional
    public Map<String, Object> getNearestVacation() {
        String empId = EmployeeUtil.getEmpId();
        LocalDateTime currentDate = LocalDateTime.now(); // 현재 시간 추가

        Optional<WorkAttitudeVacationResponseDTO> nearestVacation = vacationQueryRepository.findNearestVacation(empId, currentDate);

        if (nearestVacation.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_VACATION);
        }

        WorkAttitudeVacationResponseDTO vacation = nearestVacation.get();
        long daysUntilVacation = ChronoUnit.DAYS.between(LocalDate.now(), vacation.getStartDate().toLocalDate());

        return Map.of(
                "empId", empId,
                "nearestVacation", vacation,
                "daysUntilVacation", daysUntilVacation
        );
    }


    // 팀원 휴가 조회 (부서 기준)
    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getTeamVacationList() {
        Long deptId = employeeRepository.findDeptIdByEmpId(EmployeeUtil.getEmpId());
        if (deptId == null) { // deptId가 null이면 예외 처리
            throw new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT);
        }
        return vacationQueryRepository.findByDepartment(deptId);
    }


    // 인사팀 전체 휴가 조회
    @Transactional
    public List<WorkAttitudeVacationResponseDTO> getAllVacationList() {

        return vacationQueryRepository.findAllVacations();
    }
}
