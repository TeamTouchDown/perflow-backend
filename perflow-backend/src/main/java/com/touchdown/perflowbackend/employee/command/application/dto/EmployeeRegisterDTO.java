package com.touchdown.perflowbackend.employee.command.application.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EmployeeRegisterDTO {

    private String empId;

    private Long positionId;

    private Long jobId;

    private Long departmentId;

    private String name;

    private String gender;

    private String rrn; // 주민등록번호

    private Long pay;

    private String address;

    private String contact;

    private String email;

    private EmployeeStatus Status;

    private LocalDate joinDate;
}
