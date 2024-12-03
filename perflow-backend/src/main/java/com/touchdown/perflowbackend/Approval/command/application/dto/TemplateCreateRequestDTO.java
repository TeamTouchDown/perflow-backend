package com.touchdown.perflowbackend.Approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TemplateCreateRequestDTO {

    private final String name;

    private final String description;

    private List<TemplateFieldCreateRequestDTO> fields;


}
