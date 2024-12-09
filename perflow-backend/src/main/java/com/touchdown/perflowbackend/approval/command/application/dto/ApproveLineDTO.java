package com.touchdown.perflowbackend.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApproveLineDTO {

    @JsonProperty("approveType")
    private final ApproveType approveType;

    @JsonProperty("order")
    private final Integer approveLineOrder;

    @JsonProperty("pllGroupId")
    private final Long pllGroupId;

    @JsonProperty("departments")
    private final List<Long> departments;

    @JsonProperty("employees")
    private final List<String> Employees;
}
