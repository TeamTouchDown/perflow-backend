package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAttendanceQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAttendanceQueryService {

    private final WorkAttitudeAttendanceQueryRepository attendanceRepository;
    private final EmployeeQueryRepository employeeRepository;

    // 사원 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForEmployee() {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
        List<Attendance> records = attendanceRepository.findByEmpId(empId);
        return calculateWeeklySummary(records);
    }

    // 사원 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForEmployee() {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
        List<Attendance> records = attendanceRepository.findByEmpId(empId);
        return calculateMonthlySummary(records);
    }

    // 팀장 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForTeam() {

        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId); // 팀장 부서 ID 조회
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId); // 팀원들 조회
        List<Attendance> records = attendanceRepository.findByEmpIds(teamEmpIds);
        return calculateWeeklySummary(records);
    }

    // 팀장 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForTeam() {

        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId);
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId);
        List<Attendance> records = attendanceRepository.findByEmpIds(teamEmpIds);
        return calculateMonthlySummary(records);
    }

    // 인사팀 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForAllEmployees() {

        List<Attendance> records = attendanceRepository.findAll();
        return calculateWeeklySummary(records);
    }

    // 인사팀 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForAllEmployees() {

        List<Attendance> records = attendanceRepository.findAll();
        return calculateMonthlySummary(records);
    }

    // 주차별 요약 계산 (점심시간 1시간 제외)

    private List<WorkAttitudeAttendanceSummaryResponseDTO> calculateWeeklySummary(List<Attendance> records) {

        List<WorkAttitudeAttendanceSummaryResponseDTO> summaries = new ArrayList<>();
        LocalDate initialStart = records.stream()
                .map(a -> a.getCheckInDateTime().toLocalDate())
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
        LocalDate endDate = records.stream()
                .map(a -> a.getCheckOutDateTime().toLocalDate())
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate currentStart = initialStart;
        int weekIndex = 1;

        while (currentStart.isBefore(endDate)) {

            LocalDate currentEnd = currentStart.plus(6, ChronoUnit.DAYS);

            final LocalDate startOfWeek = currentStart;
            final LocalDate endOfWeek = currentEnd;

            long totalMinutes = records.stream()
                    .filter(a -> !a.getCheckInDateTime().toLocalDate().isBefore(startOfWeek) &&
                            !a.getCheckInDateTime().toLocalDate().isAfter(endOfWeek))
                    .mapToLong(a -> {
                        long workDuration = ChronoUnit.MINUTES.between(a.getCheckInDateTime(), a.getCheckOutDateTime());
                        // 점심시간 1시간 제외
                        return workDuration - 60; // 점심시간 60분 제외
                    })
                    .sum();

            summaries.add(new WorkAttitudeAttendanceSummaryResponseDTO(weekIndex + "주차", (int) (totalMinutes / 60), (int) (totalMinutes % 60)));
            weekIndex++;
            currentStart = currentEnd.plusDays(1); // 다음 주차로 넘어가기
        }
        return summaries;
    }

    // 월별 요약 계산 (점심시간 1시간 제외)
    private List<WorkAttitudeAttendanceSummaryResponseDTO> calculateMonthlySummary(List<Attendance> records) {

        return records.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInDateTime().getMonth()))
                .entrySet()
                .stream()
                .map(entry -> {
                    long totalMinutes = entry.getValue().stream()
                            .mapToLong(a -> {
                                long workDuration = ChronoUnit.MINUTES.between(a.getCheckInDateTime(), a.getCheckOutDateTime());
                                // 점심시간 1시간 제외
                                return workDuration - 60;
                            })
                            .sum();
                    return new WorkAttitudeAttendanceSummaryResponseDTO(entry.getKey().name(), (int) (totalMinutes / 60), (int) (totalMinutes % 60));
                })
                .collect(Collectors.toList());
    }
}
