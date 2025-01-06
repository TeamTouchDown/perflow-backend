package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.util.DateUtil;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.query.dto.SimpleAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAnnualQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAttendanceQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeVacationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WorkAttitudeAttendanceQueryService {

    private final WorkAttitudeAttendanceQueryRepository attendanceRepository;
    private final EmployeeQueryRepository employeeRepository;
    private final WorkAttitudeVacationQueryRepository vacationRepository;
    private final WorkAttitudeAnnualQueryRepository annualRepository;

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

    @Transactional
    public List<SimpleAttendanceSummaryResponseDTO> getSimpleMonthlySummaryForAllEmployees() {
        List<Attendance> records = attendanceRepository.findAll();
        return calculateSimpleMonthlySummary(records); // 전체 직원
    }

    private List<SimpleAttendanceSummaryResponseDTO> calculateSimpleMonthlySummary(List<Attendance> records) {
        // 모든 사원의 empId 추출
        Set<String> employeeIds = records.stream()
                .map(a -> a.getEmpId().getEmpId())
                .collect(Collectors.toSet());

        // 모든 사원의 상세 정보 가져오기
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        Map<String, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getEmpId, e -> e));

        // 사원별로 월별 출퇴근 기록 그룹화
        Map<String, Map<YearMonth, List<Attendance>>> attendanceGrouped = records.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEmpId().getEmpId(),
                        Collectors.groupingBy(a -> YearMonth.from(a.getCheckInDateTime()))
                ));

        List<SimpleAttendanceSummaryResponseDTO> summaries = new ArrayList<>();

        // 모든 사원에 대해 처리
        for (String empId : employeeIds) {
            Employee employee = employeeMap.get(empId);
            if (employee == null) {
                // 사원 정보가 없는 경우 처리 (필요 시 예외를 던지거나 로그를 남길 수 있습니다)
                continue;
            }

            Map<YearMonth, List<Attendance>> empMonthlyRecords = attendanceGrouped.getOrDefault(empId, Collections.emptyMap());

            for (Map.Entry<YearMonth, List<Attendance>> monthEntry : empMonthlyRecords.entrySet()) {
                YearMonth yearMonth = monthEntry.getKey();
                List<Attendance> monthRecords = monthEntry.getValue();

                // 해당 월의 전체 근무일 계산 (주말 제외)
                LocalDate firstDay = yearMonth.atDay(1);
                LocalDate lastDay = yearMonth.atEndOfMonth();

                List<LocalDate> workingDays = firstDay.datesUntil(lastDay.plusDays(1))
                        .filter(date -> !DateUtil.isWeekend(date))
                        .collect(Collectors.toList());

                // 해당 월의 휴가일 목록 생성
                List<WorkAttitudeVacationResponseDTO> vacations = vacationRepository.findDetailsByEmpId(empId);
                List<WorkAttitudeAnnualResponseDTO> annuals = annualRepository.findByEmpId(empId);
                Set<LocalDate> leaveDays = getLeaveDays(vacations, annuals, firstDay, lastDay);

                // 날짜별 출근 기록 매핑
                Map<LocalDate, List<Attendance>> attendanceMap = monthRecords.stream()
                        .collect(Collectors.groupingBy(a -> a.getCheckInDateTime().toLocalDate()));

                int absenceCount = 0;
                int tardinessCount = 0;

                for (LocalDate date : workingDays) {
                    // 휴가일인 경우 결근으로 처리하지 않음
                    if (leaveDays.contains(date)) {
                        continue;
                    }

                    List<Attendance> attendances = attendanceMap.get(date);
                    if (attendances == null || attendances.isEmpty()) {
                        absenceCount++;
                    } else {
                        for (Attendance attendance : attendances) {
                            // 지각 여부 확인 (09:00 이후 출근 시 지각)
                            LocalTime checkInTime = attendance.getCheckInDateTime().toLocalTime();
                            if (checkInTime.isAfter(LocalTime.of(9, 0))) {
                                tardinessCount++;
                            }
                        }
                    }
                }

                // DTO 생성 및 리스트에 추가
                summaries.add(SimpleAttendanceSummaryResponseDTO.builder()
                        .empId(empId)
                        .empName(employee.getName())
                        .year(yearMonth.getYear())
                        .month(yearMonth.getMonthValue())
                        .absenceCount(absenceCount)
                        .tardinessCount(tardinessCount)
                        .build());
            }
        }

        return summaries;
    }


    private Set<LocalDate> getLeaveDays(List<WorkAttitudeVacationResponseDTO> vacations,
                                        List<WorkAttitudeAnnualResponseDTO> annuals,
                                        LocalDate startDate,
                                        LocalDate endDate) {
        Set<LocalDate> leaveDays = new HashSet<>();

        // Vacation에서 휴가일 추가
        vacations.forEach(vacation -> {
            LocalDate start = vacation.getVacationStart().toLocalDate();
            LocalDate end = vacation.getVacationEnd().toLocalDate();

            // 전체 기간과 겹치는 날짜만 추가
            if (isSamePeriodOrOverlap(start, end, startDate, endDate)) {
                LocalDate effectiveStart = start.isBefore(startDate) ? startDate : start;
                LocalDate effectiveEnd = end.isAfter(endDate) ? endDate : end;

                leaveDays.addAll(effectiveStart.datesUntil(effectiveEnd.plusDays(1))
                        .filter(date -> !DateUtil.isWeekend(date))
                        .collect(Collectors.toSet()));
            }
        });

        // Annual에서 연차일 추가
        annuals.forEach(annual -> {
            LocalDate start = annual.getAnnualStart().toLocalDate();
            LocalDate end = annual.getAnnualEnd().toLocalDate();

            // 전체 기간과 겹치는 날짜만 추가
            if (isSamePeriodOrOverlap(start, end, startDate, endDate)) {
                LocalDate effectiveStart = start.isBefore(startDate) ? startDate : start;
                LocalDate effectiveEnd = end.isAfter(endDate) ? endDate : end;

                leaveDays.addAll(effectiveStart.datesUntil(effectiveEnd.plusDays(1))
                        .filter(date -> !DateUtil.isWeekend(date))
                        .collect(Collectors.toSet()));
            }
        });

        return leaveDays;
    }

    private boolean isSamePeriodOrOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !end1.isBefore(start2) && !start1.isAfter(end2);
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
                                long adjustedDuration = workDuration - 60;  // 점심시간 60분 제외
                                return Math.max(adjustedDuration, 0);
                            })
                    ));
            employeeMinutes.forEach((empId, totalMinutes) -> {
                String empName = employeeRepository.findById(empId)
                        .orElseThrow(() ->
                                new CustomException(ErrorCode.NOT_FOUND_EMP))
                        .getName();
                int year = startOfWeek.getYear();
                summaries.add(new WorkAttitudeAttendanceSummaryResponseDTO(
                        weekIndex[0] + "주차",
                        (int) (totalMinutes / 60),
                        (int) (totalMinutes % 60),
                        empId,
                        empName,
                        year
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

                                int year = empEntry.getValue().get(0).getCheckInDateTime().getYear();


                                // DTO 생성하여 반환
                                return new WorkAttitudeAttendanceSummaryResponseDTO(
                                        entry.getKey().name(), // 월
                                        (int) (totalMinutes / 60), // 총 시간
                                        (int) (totalMinutes % 60), // 총 분
                                        empId, // empId
                                        empName, // empName
                                        year

                                );
                            });
                })
                .collect(Collectors.toList());
    }

}