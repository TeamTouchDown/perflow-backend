package com.touchdown.perflowbackend.authority.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthorityEmployeeRequestDTO {

    private final String empId;
    private final Long authorityId;
}
