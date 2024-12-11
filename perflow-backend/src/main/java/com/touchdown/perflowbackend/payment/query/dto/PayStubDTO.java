package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayStubDTO {

    private PayrollDTO payStub;

    public PayStubDTO(PayrollDTO payStub) {

        this.payStub = payStub;

    }

}
