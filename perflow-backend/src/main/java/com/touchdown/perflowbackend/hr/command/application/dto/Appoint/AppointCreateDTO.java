package com.touchdown.perflowbackend.hr.command.application.dto.Appoint;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AppointCreateDTO {

    private Long appointId;

    private String empId;

    private Type type;

    private Long after;

    private LocalDateTime appointDatetime;
}
