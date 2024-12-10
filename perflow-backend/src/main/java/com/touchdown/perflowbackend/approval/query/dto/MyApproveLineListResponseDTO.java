package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyApproveLineListResponseDTO {

    private final Long approveLineId;

    private final String name;

    private final String description;

    private List<String> employees; // 결재선 - 사원

    private List<String> departments;   // 결재선 - 부서

    private final ApproveTemplateType approveTemplateType;

    private final LocalDateTime createDatetime;
}
