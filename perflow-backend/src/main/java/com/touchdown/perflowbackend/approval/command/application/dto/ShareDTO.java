package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ShareDTO {

    private final List<Long> departments;

    private final List<String> employees;
}
