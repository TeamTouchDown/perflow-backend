package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
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
    public double getAnnualBalance() {

        String empId = EmployeeUtil.getEmpId();
        int currentYear = LocalDate.now().getYear();

        double providedAnnual = calculateProvidedAnnualCount(empId, currentYear);
        double usedAnnual = calculateUsedAnnualCount(empId, currentYear);

        return  (providedAnnual - usedAnnual);
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
    private double calculateProvidedAnnualCount(String empId, int year) {

        LocalDate joinDate = employeeRepository.findJoinDateByEmpId(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

        int baseAnnual = 15;
        int maxAnnual = 25;

        int years = year - joinDate.getYear();
        int additionalAnnual = (years / 3);

        return Math.min(baseAnnual + additionalAnnual, maxAnnual);
    }

    // 사용한 연차 개수 계산
    private double calculateUsedAnnualCount(String empId, int year) {

        List<Annual> usedAnnuals = annualRepository.findConfirmedAnnualsByYearAndEndDate(empId, year);

        double totalUsedDays = 0.0;

        LocalDate today = LocalDate.now(); // 오늘 날짜

        for (Annual annual : usedAnnuals) {
            if (annual.getAnnualType() == AnnualType.MORNINGHALF || annual.getAnnualType() == AnnualType.AFTERNOONHALF) {
                // 반차는 무조건 0.5일 처리
                totalUsedDays += 0.5;
            } else {
                // 시작일과 종료일이 같으면 1일로 처리
                if (annual.getAnnualStart().toLocalDate().isEqual(annual.getAnnualEnd().toLocalDate())) {
                    totalUsedDays += 1; // 같은 날이면 무조건 1일
                } else {
                    // 시작일부터 종료일까지 계산
                    long days = java.time.temporal.ChronoUnit.DAYS.between(
                            annual.getAnnualStart().toLocalDate(),
                            annual.getAnnualEnd().toLocalDate()
                    ) + 1; // 하루 추가 계산
                    totalUsedDays += days;
                }
            }
        }
        return totalUsedDays;// 사용한 연차 반환
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
// 테스트 코드 작성시 연도가 달라질 경우 잔여 연차 개수가 제대로 바뀌어서 나오는지 확인
