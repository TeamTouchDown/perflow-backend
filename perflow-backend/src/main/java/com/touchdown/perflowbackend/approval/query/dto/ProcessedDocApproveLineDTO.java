package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProcessedDocApproveLineDTO {

    private final Long approveLineId;

    private final Long groupId;

    private final ApproveType approveType;    // 결재 방식(동의, 참조, 병렬...)

    private final Long approveLineOrder; // 결재선 내 순서

    private final Long pllGroupId;  // 병렬 그룹 id

    private final ApproveTemplateType approveTemplateType; // MY_APPROVE_LINE, MANUAL

    private final List<ProcessedDocApproveSbjDTO> approveSbjs;  // 결재 대상 리스트
}
