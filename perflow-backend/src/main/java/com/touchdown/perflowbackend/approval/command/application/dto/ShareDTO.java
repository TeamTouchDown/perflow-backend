package com.touchdown.perflowbackend.approval.command.application.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ObjType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ShareDTO {

    private final ObjType shareObjType;

    private final List<Long> departments;

    private final List<String> employees;
}
