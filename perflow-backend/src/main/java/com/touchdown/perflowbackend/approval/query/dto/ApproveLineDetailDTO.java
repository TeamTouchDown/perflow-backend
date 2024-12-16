package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApproveLineDetailDTO {

    private final Long approveLineId;

    private final ApproveType approveType;

    private final Long approveLineOrder;

    private final List<ApproveSbjDTO> approveSbjs;
}
