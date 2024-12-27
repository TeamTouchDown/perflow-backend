package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForEmployeeSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeResponseDTO;
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
    private final WorkAttitudeOvertimeQueryRepository repository;
    private final EmployeeQueryRepository employeeQueryRepository;

    private void verifyCurrentUser(String empId) {
        String currentUserId = EmployeeUtil.getEmpId();
        if (!currentUserId.equals(empId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    @Transactional
    public List<WorkAttitudeOvertimeForTeamLeaderSummaryDTO> getOvertimeSummaryForAllEmployees() {
        List<Overtime> overtimes = repository.findAllNotDeleted();

        Map<String, List<Overtime>> groupedByEmployee = overtimes.stream()
                .collect(Collectors.groupingBy(overtime -> overtime.getEmpId().getEmpId()));

        List<Employee> employees = employeeQueryRepository.findActiveEmployees();

        Map<String, String> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getEmpId, Employee::getName));

        return groupedByEmployee.entrySet().stream()
                .map(entry -> {
                    String empId = entry.getKey();
                    List<Overtime> employeeOvertimes = entry.getValue();

                    long nightHours = calculateTotalHours(employeeOvertimes, OvertimeType.NIGHT);
                    long holidayHours = calculateTotalHours(employeeOvertimes, OvertimeType.HOLIDAY);
                    long extendedHours = calculateTotalHours(employeeOvertimes, OvertimeType.EXTENDED);
                    long totalHours = nightHours + holidayHours + extendedHours;

                    String employeeName = employeeMap.get(empId);

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
    public WorkAttitudeOvertimeResponseDTO getOvertimeSummaryForEmployee(String empId) {
        verifyCurrentUser(empId);

        List<Overtime> overtimes = repository.findByEmpIdNotDeleted(empId);

        Overtime overtime = overtimes.get(0);

        return WorkAttitudeOvertimeResponseDTO.builder()
                .overtimeId(overtime.getOvertimeId())
                .empId(overtime.getEmpId().getEmpId())
                .empName(overtime.getEmpId().getName())
                .approverId(overtime.getApprover().getEmpId())
                .approverName(overtime.getApprover().getName())
                .overtimeType(overtime.getOvertimeType())
                .enrollOvertime(overtime.getEnrollOvertime())
                .overtimeStart(overtime.getOvertimeStart())
                .overtimeEnd(overtime.getOvertimeEnd())
                .overtimeStatus(overtime.getOvertimeStatus())
                .overtimeRejectReason(overtime.getOvertimeRejectReason())
                .isOvertimeRetroactive(overtime.getIsOvertimeRetroactive())
                .overtimeRetroactiveReason(overtime.getOvertimeRetroactiveReason())
                .overtimeRetroactiveStatus(overtime.getOvertimeRetroactiveStatus())
                .status(overtime.getStatus())
                .build();
    }

    @Transactional
    public List<WorkAttitudeOvertimeResponseDTO> getAllOvertimes() {
        return repository.findAllNotDeleted().stream()
                .map(Overtime::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<WorkAttitudeOvertimeResponseDTO> getOvertimeForTeam() {
        String currentEmpId = EmployeeUtil.getEmpId();
        Long deptId = employeeQueryRepository.findDeptIdByEmpId(currentEmpId);

        return repository.findTeamOvertimes(deptId).stream()
                .map(Overtime::toResponseDTO)
                .toList();
    }



    @Transactional
    public List<WorkAttitudeOvertimeResponseDTO> getOvertimeForEmployee(String empId) {
        verifyCurrentUser(empId);
        return repository.findByEmpIdNotDeleted(empId).stream()
                .map(Overtime::toResponseDTO)
                .toList();
    }
}
