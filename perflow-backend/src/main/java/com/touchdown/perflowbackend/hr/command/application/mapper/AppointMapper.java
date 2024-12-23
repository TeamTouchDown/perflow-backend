package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.hr.query.dto.AppointResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class AppointMapper {

    public static Appoint toEntity(AppointCreateDTO appointmentCreateDTO, Employee employee, String after, String before) {

        return Appoint.builder()
                .appointCreateDTO(appointmentCreateDTO)
                .emp(employee)
                .after(after)
                .before(before)
                .build();
    }

    public static List<AppointResponseDTO> toResponseDTO(List<Appoint> appointments) {

        List<AppointResponseDTO> responseDTOs = new ArrayList<>();
        for (Appoint appointment : appointments) {
            responseDTOs.add(
                    AppointResponseDTO.builder()
                            .empName(appointment.getEmp().getName())
                            .type(appointment.getType())
                            .after(appointment.getAfter())
                            .before(appointment.getBefore())
                            .appointDate(appointment.getAppointDate())
                            .build()
            );
        }
        return responseDTOs;
    }
}
