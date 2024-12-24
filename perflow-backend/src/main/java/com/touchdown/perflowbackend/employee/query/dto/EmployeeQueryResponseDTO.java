package com.touchdown.perflowbackend.employee.query.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Builder
public class EmployeeQueryResponseDTO {

    private final String empId;

    private final String position;

    private final String job;

    private final String department;

    private final String name;

}
