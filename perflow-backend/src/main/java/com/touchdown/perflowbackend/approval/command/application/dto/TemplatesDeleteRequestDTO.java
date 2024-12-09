package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TemplatesDeleteRequestDTO {

    private final List<Long> templateIds;
}
