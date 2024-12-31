package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyApproveLineCreateRequestDTO {

    private final String name;  // 결재선 이름

    private final String description;   // 결재선 설명

    private final List<ApproveLineRequestDTO> approveLines;    // 결재선 정보
}
