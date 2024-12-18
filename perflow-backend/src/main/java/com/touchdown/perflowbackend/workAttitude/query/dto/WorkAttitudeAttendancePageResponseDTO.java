package com.touchdown.perflowbackend.workAttitude.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorkAttitudeAttendancePageResponseDTO {

    private List<WorkAttitudeAttendanceSummaryResponseDTO> summaries;

    private int totalPages;

    private int totalItems;

    private int currentPage;

    private int pageSize;

}
