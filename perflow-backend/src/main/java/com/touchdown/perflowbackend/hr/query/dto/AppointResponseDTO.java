package com.touchdown.perflowbackend.hr.query.dto;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AppointResponseDTO {

    private String empName;

    private Type type;

    private String before;

    private String after;

    private LocalDate appointDate;
}
