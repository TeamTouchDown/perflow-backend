package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 나의 결재선 상세 조회 시 response
@Getter
@Builder
public class ApproveLineResponseDTO {

    @JsonProperty("groupId")
    private final Long groupId;

    @JsonProperty("approveLineId")
    private Long approveLineId;

    @JsonProperty("approveType")
    private final ApproveType approveType;

    @JsonProperty("approveLineOrder")
    private final Long approveLineOrder;

    @JsonProperty("pllGroupId")
    private final Long pllGroupId;

    @JsonProperty("approveTemplateTypes")
    private final ApproveTemplateType approveTemplateType;

    @JsonProperty
    private final List<ApproveSbjDTO> approveSbjs;
}
