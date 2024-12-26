package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InboxDocApproveLineDTO {

    private final Long approveLineId; // 결재선 ID

    private final Long groupId; // 결재선 그룹 ID

    private final ApproveType approveType; // 결재 방식

    private final Long approveLineOrder; // 결재선 순서

    private final Long pllGroupId; // 병렬 그룹 ID

    private final ApproveTemplateType approveTemplateType; // 템플릿 타입

    private final List<InboxDocApproveSbjDTO> approveSbjs; // 결재 대상 정보
}
