package com.touchdown.perflowbackend.payment.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PayrollListResponseDTO {

    private List<PayrollResponseDTO> payrolls;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private int pageSize;

}
