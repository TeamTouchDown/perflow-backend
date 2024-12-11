package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class EvalutionListDTO {

    private final String perfoedEmpId;

    private final List<EvalutionDetailDTO> answers;
}
