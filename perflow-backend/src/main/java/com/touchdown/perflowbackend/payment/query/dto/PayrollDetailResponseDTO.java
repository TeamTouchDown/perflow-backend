package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PayrollDetailResponseDTO {

    private List<PayrollDTO> payrolls;

    public PayrollDetailResponseDTO(List<PayrollDTO> payrolls) {

        this.payrolls = payrolls;

    }

}
