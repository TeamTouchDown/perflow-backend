package com.touchdown.perflowbackend.approval.command.application.dto;

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

    private List<Boolean> isRepeatedList;   // 각 필드의 반복 가능 여부 리스트

}
