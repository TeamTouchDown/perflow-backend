package com.touchdown.perflowbackend.hr.command.application.dto.Appoint;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppointCreateDTO {

    private String empId;

    private Type type;

    private Long after;
}