package com.touchdown.perflowbackend.payment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PayStubDTO {

    private PayrollDTO payStub;

}
