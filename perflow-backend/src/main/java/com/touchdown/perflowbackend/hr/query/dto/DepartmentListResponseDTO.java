package com.touchdown.perflowbackend.hr.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
// 전체 목록 조회에 사용될 DTO
public class DepartmentListResponseDTO {

    private Long deptId;

    private Long managedDeptId;

    private String name;

    private String responsibility;

    private String pic;

    private String contact;
}
