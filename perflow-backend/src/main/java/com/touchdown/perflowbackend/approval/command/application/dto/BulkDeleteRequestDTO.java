package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BulkDeleteRequestDTO {

    private final List<Long> groupIds;
}
