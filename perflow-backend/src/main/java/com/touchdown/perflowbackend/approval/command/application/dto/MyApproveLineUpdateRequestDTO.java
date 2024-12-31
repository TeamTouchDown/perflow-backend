package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyApproveLineUpdateRequestDTO {

    private final Long groupId;

    private final String name;  // 결재선 이름

    private final String description;   // 결재선 설명

    private final List<ApproveLineRequestDTO> approveLines;    // 결재선 정보
}
