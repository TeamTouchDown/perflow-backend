package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;

public class AppointMapper {

    public static Appoint toEntity(AppointCreateDTO appointmentCreateDTO, Employee employee) {

        return Appoint.builder()
                .appointCreateDTO(appointmentCreateDTO)
                .emp(employee)
                .build();
    }

}
