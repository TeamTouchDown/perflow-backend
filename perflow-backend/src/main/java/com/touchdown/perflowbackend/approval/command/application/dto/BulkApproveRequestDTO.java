package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BulkApproveRequestDTO {

    private final List<ApprovalRequestDTO> approvals;
}
