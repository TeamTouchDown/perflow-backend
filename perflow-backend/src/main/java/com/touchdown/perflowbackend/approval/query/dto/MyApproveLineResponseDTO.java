package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyApproveLineResponseDTO {

    private final Long approveLineId;   // 나의 결재선 id

    private final ApproveType approveType;    // 결재 타입(SEQ, PLL, CC...)

    private final ApproveTemplateType approveTemplateType;  // MY_APPROVE_LINE, MANUAL

    private final Integer approveLineOrder;   // 결재 순서
}
