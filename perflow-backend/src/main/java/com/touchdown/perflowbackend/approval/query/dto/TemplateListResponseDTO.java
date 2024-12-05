package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class TemplateListResponseDTO {

    private final List<TemplateResponseDTO> templates;

    private final Integer currentPage;

    private final Integer totalPages;

//    private final Long totalItems;
}
