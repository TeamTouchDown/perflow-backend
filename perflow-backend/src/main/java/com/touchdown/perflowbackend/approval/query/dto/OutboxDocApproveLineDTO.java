package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OutboxDocApproveLineDTO {

    private final Long approveLineId;

    private final Long groupId;

    private final String approveType;

    private final Long approveLineOrder;

    private final String status;

    private final List<OutboxDocApproveSbjDTO> approveSbjs;
}
