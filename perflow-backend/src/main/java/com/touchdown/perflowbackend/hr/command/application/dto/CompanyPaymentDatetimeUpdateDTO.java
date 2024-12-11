package com.touchdown.perflowbackend.hr.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CompanyPaymentDatetimeUpdateDTO {

    private final Integer date;
}
