package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class WaitingDocListResponseDTO {

    private final Long docId;

    private final String title;

    private final String createUserName;

    private final LocalDateTime createDatetime;
}
