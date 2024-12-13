package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateFieldDTO {

    private final Long templateFieldId;

    private final Object userValue;   // 사용자의 입력 값(텍스트, 날짜, ...)
}
