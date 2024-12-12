package com.touchdown.perflowbackend.payment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SeverancePayDetailResponseDTO {

    private List<SeverancePayDTO> severancePays;

}
