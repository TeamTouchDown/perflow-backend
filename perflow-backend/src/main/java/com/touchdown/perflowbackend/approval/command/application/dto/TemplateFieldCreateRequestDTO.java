package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemplateFieldCreateRequestDTO {

    private final String name;

    private final String type;

    private final Boolean isReq;

    private final String defaultValue;
}
