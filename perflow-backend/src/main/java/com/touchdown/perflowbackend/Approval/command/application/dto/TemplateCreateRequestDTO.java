package com.touchdown.perflowbackend.Approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemplateCreateRequestDTO {

    private final String name;

    private final String description;
}
