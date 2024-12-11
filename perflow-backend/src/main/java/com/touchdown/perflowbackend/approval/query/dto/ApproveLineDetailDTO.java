package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ApproveLineDetailDTO {

    @JsonProperty("approveLineId")
    private Long approveLineId; // 나의 결재선 id

    @JsonProperty("approveType")
    private final ApproveType approveType;

    @JsonProperty("order")
    private final Integer approveLineOrder;

    @JsonProperty("pllGroupId")
    private final Long pllGroupId;

    @JsonProperty("departments")
    private final List<Long> departments;

    @JsonProperty("employees")
    private final List<String> employees;

    @JsonProperty("approveTemplateTypes")
    private final ApproveTemplateType approveTemplateTypes;

    private final List<approvesbj>
}
