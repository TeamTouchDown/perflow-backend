package com.touchdown.perflowbackend.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import com.touchdown.perflowbackend.approval.query.dto.ApproveSbjDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApproveLineRequestDTO {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("approveType")
    private final ApproveType approveType;    // 결재 방식(동의, 참조, 병렬...)

    @JsonProperty("approveLineOrder")
    private final Long approveLineOrder; // 결재선 내 순서

    @JsonProperty("pllGroupId")
    private final Long pllGroupId;  // 병렬 그룹 id

    @JsonProperty("approveTemplateTypes")   // 나의결재선에서 가져온 결재선 or 문서 작성 시 직접 설정한 결재선
    private final ApproveTemplateType approveTemplateTypes; // MY_APPROVE_LINE, MANUAL

    @JsonProperty("approveSbjs")
    private final List<ApproveSbjDTO> approveSbjs;  // 결재 대상 리스트
}
