package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeverancePayListResponseDTO {

    private List<SeverancePayResponseDTO> severancePays;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private int pageSize;

}
