package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForEmployeeSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeOvertimeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkAttitudeOvertimeQueryService {
    //각 초과근무별 업무 시간 계산 로직 만들기

    private final WorkAttitudeOvertimeQueryRepository repository;
    private final EmployeeQueryRepository employeeQueryRepository;

    @Transactional
    public List<WorkAttitudeOvertimeForTeamLeaderSummaryDTO> getOvertimeSummaryForAllEmployees() {
        List<Overtime> overtimes = repository.findAllNotDeleted();

        // 사원별로 그룹화 후 시간 계산
        Map<String, List<Overtime>> groupedByEmployee = overtimes.stream()
                .collect(Collectors.groupingBy(overtime -> overtime.getEmpId().getEmpId()));

        List<Employee> employees = employeeQueryRepository.findAll();

        Map<String, String> employee = employees.stream()
                .collect(Collectors.toMap(Employee::getEmpId, Employee::getName));

        return groupedByEmployee.entrySet().stream()
                .map(entry -> {
                    String empId = entry.getKey();
                    List<Overtime> employeeOvertimes = entry.getValue();

                    long nightHours = calculateTotalHours(employeeOvertimes, OvertimeType.NIGHT);
                    long holidayHours = calculateTotalHours(employeeOvertimes, OvertimeType.HOLIDAY);
                    long extendedHours = calculateTotalHours(employeeOvertimes, OvertimeType.EXTENDED);
                    long totalHours = nightHours + holidayHours + extendedHours;

                    // 사원 이름 가져오기
                    String employeeName = employee.get(empId);  // empId로 이름을 가져옴

                    return WorkAttitudeOvertimeForTeamLeaderSummaryDTO.builder()
                            .empId(empId)
                            .employeeName(employeeName)
                            .nightHours(nightHours)
                            .holidayHours(holidayHours)
                            .extendedHours(extendedHours)
                            .totalHours(totalHours)
                            .build();
                }).toList();
    }
    private long calculateTotalHours(List<Overtime> overtimes, OvertimeType type) {
        return overtimes.stream()
                .filter(overtime -> overtime.getOvertimeType() == type)
                .mapToLong(overtime -> Duration.between(overtime.getOvertimeStart(), overtime.getOvertimeEnd()).toHours())
                .sum();
    }




    @Transactional
    public WorkAttitudeOvertimeForEmployeeSummaryDTO getOvertimeSummaryForEmployee(String empId) {
        List<Overtime> overtimes = repository.findByEmpIdNotDeleted(empId);

        long nightHours = calculateTotalHours(overtimes, OvertimeType.NIGHT);
        long holidayHours = calculateTotalHours(overtimes, OvertimeType.HOLIDAY);
        long extendedHours = calculateTotalHours(overtimes, OvertimeType.EXTENDED);
        long totalHours = nightHours + holidayHours + extendedHours;

        return WorkAttitudeOvertimeForEmployeeSummaryDTO.builder()
                .employeeName(overtimes.get(0).getEmpId().getName())
                .nightHours(nightHours)
                .holidayHours(holidayHours)
                .extendedHours(extendedHours)
                .totalHours(totalHours)
                .build();
    }


}
