package com.touchdown.perflowbackend.Approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TemplateCreateRequestDTO {

    private final String name;

    private final String description;

    private final List<Long> fieldTypes;    // 사용자가 선택한 fieldType의 id들

    private final List<String> detailsList; // 각 필드의 JSON details들


}
