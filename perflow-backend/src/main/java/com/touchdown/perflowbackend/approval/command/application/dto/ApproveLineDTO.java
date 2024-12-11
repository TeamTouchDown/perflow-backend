package com.touchdown.perflowbackend.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ApproveLineDTO {

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
}
