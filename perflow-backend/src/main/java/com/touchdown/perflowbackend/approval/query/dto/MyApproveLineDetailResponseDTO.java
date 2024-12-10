package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyApproveLineDetailResponseDTO {

    private final Long approveLineId;

    private final String name;

    private final String description;

    private final List<ApproveLine> approveLines;


}
