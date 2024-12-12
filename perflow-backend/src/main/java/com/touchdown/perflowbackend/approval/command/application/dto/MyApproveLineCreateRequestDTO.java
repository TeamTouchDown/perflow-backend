package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyApproveLineCreateRequestDTO {

    private final String name;

    private final String description;

    private final List<ApproveLineDTO> approveLines;
}
