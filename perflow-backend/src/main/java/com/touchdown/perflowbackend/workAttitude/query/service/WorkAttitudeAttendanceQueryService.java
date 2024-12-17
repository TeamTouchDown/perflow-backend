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
import java.util.Map;
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
        return calculateWeeklySummaryWithNames(records);
    }

    // 사원 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForEmployee() {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
        List<Attendance> records = attendanceRepository.findByEmpId(empId);
        return calculateMonthlySummaryWithNames(records);
    }

    // 팀장 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForTeam() {

        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId); // 팀장 부서 ID 조회
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId); // 팀원들 조회
        List<Attendance> records = attendanceRepository.findByEmpIds(teamEmpIds);
        return calculateWeeklySummaryWithNames(records);
    }

    // 팀장 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForTeam() {

        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId);
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId);
        List<Attendance> records = attendanceRepository.findByEmpIds(teamEmpIds);
        return calculateMonthlySummaryWithNames(records);
    }

    // 인사팀 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForAllEmployees() {

        List<Attendance> records = attendanceRepository.findAll();
        return calculateWeeklySummaryWithNames(records);
    }

    // 인사팀 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForAllEmployees() {

        List<Attendance> records = attendanceRepository.findAll();
        return calculateMonthlySummaryWithNames(records);
    }

    // 주차별 근무 시간 계산 (점심시간 1시간 제외,각 사원의 이름 포함)
    private List<WorkAttitudeAttendanceSummaryResponseDTO> calculateWeeklySummaryWithNames(List<Attendance> records) {
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
        final int[] weekIndex = {1};

        while (currentStart.isBefore(endDate)) {
            LocalDate currentEnd = currentStart.plus(6, ChronoUnit.DAYS);
            final LocalDate startOfWeek = currentStart;
            final LocalDate endOfWeek = currentEnd;

            // 근무 시간을 합산하고 각 사원 정보도 포함
            Map<String, Long> employeeMinutes = records.stream()
                    .filter(a ->
                            !a.getCheckInDateTime().toLocalDate().isBefore(startOfWeek) &&
                            !a.getCheckInDateTime().toLocalDate().isAfter(endOfWeek))
                    .collect(Collectors.groupingBy(
                            a-> a.getEmpId().getEmpId(),
                            Collectors.summingLong(a ->{
                                long workDuration =
                                        ChronoUnit.MINUTES.between(
                                                a.getCheckInDateTime(),
                                                a.getCheckOutDateTime()
                                        );
                                return workDuration -60;
                            })
                    ));
            employeeMinutes.forEach((empId, totalMinutes) -> {
                        String empName = employeeRepository.findById(empId)
                                .orElseThrow(() ->
                                        new CustomException(ErrorCode.NOT_FOUND_EMP))
                                .getName();
                summaries.add(new WorkAttitudeAttendanceSummaryResponseDTO(
                        weekIndex[0] + "주차",
                        (int) (totalMinutes / 60),
                        (int) (totalMinutes % 60),
                        empId,
                        empName
                ));
                    });
            weekIndex[0]++;
            currentStart = currentEnd.plusDays(1);  // 다음 주차로 넘어가기
        }
        return summaries;
    }

    // 월별 근무 시간 계산 (각 사원의 이름 포함)
    private List<WorkAttitudeAttendanceSummaryResponseDTO> calculateMonthlySummaryWithNames(List<Attendance> records) {
        return records.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInDateTime().getMonth())) // 월별로 그룹화
                .entrySet()
                .stream()
                .flatMap(entry -> {
                    // 각 월에 대해 사원별로 계산
                    return entry.getValue().stream()
                            .collect(Collectors.groupingBy(a -> a.getEmpId().getEmpId())) // empId로 그룹화 (사원별로 그룹화)
                            .entrySet()
                            .stream()
                            .map(empEntry -> {
                                long totalMinutes = empEntry.getValue().stream()
                                        .mapToLong(a -> {
                                            long workDuration = ChronoUnit.MINUTES.between(a.getCheckInDateTime(), a.getCheckOutDateTime());
                                            // 점심시간 1시간 제외
                                            return workDuration - 60;
                                        })
                                        .sum();
                                String empId = empEntry.getKey(); // 사원의 empId
                                String empName = empEntry.getValue().get(0).getEmpId().getName(); // 사원의 이름

                                // DTO 생성하여 반환
                                return new WorkAttitudeAttendanceSummaryResponseDTO(
                                        entry.getKey().name(), // 월
                                        (int) (totalMinutes / 60), // 총 시간
                                        (int) (totalMinutes % 60), // 총 분
                                        empId, // empId
                                        empName // empName
                                );
                            });
                })
                .collect(Collectors.toList());
    }

}
