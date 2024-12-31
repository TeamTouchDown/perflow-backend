package com.touchdown.perflowbackend.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeResponseList {

    List<EmployeeQueryResponseDTO> employeeList;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private int pageSize;

    @Builder
    public EmployeeResponseList(List<EmployeeQueryResponseDTO> employeeList, int totalPages, int totalItems, int currentPage, int pageSize) {

        this.employeeList = employeeList;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
