package com.touchdown.perflowbackend.hr.command.application.dto.company;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CompanyAnnualCountUpdateDTO {

    @Min(12L)
    private final Integer companyAnnualCount;
}
