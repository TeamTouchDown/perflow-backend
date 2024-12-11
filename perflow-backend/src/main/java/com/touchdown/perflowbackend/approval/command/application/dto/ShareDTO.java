package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ShareDTO {

    private final List<Long> departments;

    private final List<String> employees;
}
