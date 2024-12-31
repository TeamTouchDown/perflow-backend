package com.touchdown.perflowbackend.hr.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AppointResponseListDTO {

    List<AppointResponseDTO> appointResponseList;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private int pageSize;
}

