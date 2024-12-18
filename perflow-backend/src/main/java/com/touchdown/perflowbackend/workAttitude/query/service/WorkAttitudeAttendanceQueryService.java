package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendancePageResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAttendanceQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class WorkAttitudeAttendanceQueryService {

    private final WorkAttitudeAttendanceQueryRepository attendanceRepository;
    private final EmployeeQueryRepository employeeRepository;

    // 사원 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForEmployee(Pageable pageable) {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        Page<Attendance> page = attendanceRepository.findByEmpId(empId,pageable);
        /*List<Attendance> records = page.getContent();

        List<WorkAttitudeAttendanceSummaryResponseDTO> summaries = calculateWeeklySummaryWithNames(records);*/

        return calculateWeeklySummaryWithNames(page.getContent());
    }
    // 사원 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForEmployee(Pageable pageable) {
        String empId = EmployeeUtil.getEmpId();
        employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        Page<Attendance> page = attendanceRepository.findByEmpId(empId, pageable);
        return calculateMonthlySummaryWithNames(page.getContent());
    }

    // 팀장 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForTeam(Pageable pageable) {
        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId);
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId);

        Page<Attendance> page = attendanceRepository.findByEmpIds(teamEmpIds, pageable);
        return calculateWeeklySummaryWithNames(page.getContent());
    }

    // 팀장 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForTeam(Pageable pageable) {
        String leaderEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeRepository.findDeptIdByLeaderEmpId(leaderEmpId);
        List<String> teamEmpIds = employeeRepository.findEmpIdsByDeptId(deptId);

        Page<Attendance> page = attendanceRepository.findByEmpIds(teamEmpIds, pageable);
        return calculateMonthlySummaryWithNames(page.getContent());
    }

    // 인사팀 주차별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getWeeklySummaryForAllEmployees(Pageable pageable) {
        Page<Attendance> page = attendanceRepository.findAll(pageable);
        return calculateWeeklySummaryWithNames(page.getContent());
    }

    // 인사팀 월별 조회
    @Transactional
    public List<WorkAttitudeAttendanceSummaryResponseDTO> getMonthlySummaryForAllEmployees(Pageable pageable) {
        Page<Attendance> page = attendanceRepository.findAll(pageable);
        return calculateMonthlySummaryWithNames(page.getContent());
    }

    // 주차별 근무 시간 계산 (점심시간 1시간 제외, 각 사원의 이름 포함)
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

            log.info("주차 범위: {} ~ {}", startOfWeek, endOfWeek);

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
            currentStart = currentEnd.plusDays(1);
        }
        return summaries;
    }

    // 월별 근무 시간 계산 (각 사원의 이름 포함)
    private List<WorkAttitudeAttendanceSummaryResponseDTO> calculateMonthlySummaryWithNames(List<Attendance> records) {
        return records.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInDateTime().getMonth()))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .collect(Collectors.groupingBy(a -> a.getEmpId().getEmpId()))
                        .entrySet().stream()
                        .map(empEntry -> {
                            long totalMinutes = empEntry.getValue().stream()
                                    .mapToLong(a -> ChronoUnit.MINUTES.between(a.getCheckInDateTime(), a.getCheckOutDateTime()) - 60)
                                    .sum();
                            String empId = empEntry.getKey();
                            String empName = empEntry.getValue().get(0).getEmpId().getName();

                            return new WorkAttitudeAttendanceSummaryResponseDTO(
                                    entry.getKey().name(),
                                    (int) (totalMinutes / 60),
                                    (int) (totalMinutes % 60),
                                    empId,
                                    empName
                            );
                        }))
                .collect(Collectors.toList());
    }

    // 공통 로직: Page<Attendance>를 WorkAttitudeAttendancePageResponseDTO로 변환
    private WorkAttitudeAttendancePageResponseDTO mapToPageResponseDTO(Page<Attendance> page, String periodLabel) {
        List<WorkAttitudeAttendanceSummaryResponseDTO> summaries = page.getContent().stream()
                .collect(Collectors.groupingBy(a -> a.getEmpId().getEmpId()))
                .entrySet()
                .stream()
                .map(entry -> {
                    long totalMinutes = entry.getValue().stream()
                            .mapToLong(a -> ChronoUnit.MINUTES.between(a.getCheckInDateTime(), a.getCheckOutDateTime()) - 60)
                            .sum();
                    String empId = entry.getKey();
                    String empName = employeeRepository.findById(empId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP))
                            .getName();

                    return new WorkAttitudeAttendanceSummaryResponseDTO(
                            periodLabel,  // 주차 정보 또는 월 정보
                            (int) (totalMinutes / 60),  // 총 시간
                            (int) (totalMinutes % 60),  // 총 분
                            empId,  // 사원 ID
                            empName // 사원 이름
                    );
                }).collect(Collectors.toList());

        return WorkAttitudeAttendancePageResponseDTO.builder()
                .summaries(summaries)
                .totalPages(page.getTotalPages())
                .totalItems((int) page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();
    }

}
