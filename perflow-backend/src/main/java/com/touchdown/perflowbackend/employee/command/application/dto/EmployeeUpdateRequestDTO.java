package com.touchdown.perflowbackend.employee.command.application.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeUpdateRequestDTO {

    private String empId;

    private String name;

    private String gender;

    private String rrn; // 주민등록번호

    private Long pay;

    private String address;

    private String contact;

    private String email;

    private LocalDate joinDate;
}
