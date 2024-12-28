package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualRetroactiveStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkAttitudeAnnualResponseDTO {

    private Long annualId;

    private String empId;

    private String empName;

    private String approverId;

    private String approverName;

    private LocalDateTime enrollAnnual;

    private LocalDateTime annualStart;

    private LocalDateTime annualEnd;

    private AnnualType annualType;

    private Status status;

    private String annualRejectReason;

    private Boolean isAnnualRetroactive;

    private String annualRetroactiveReason;

    private AnnualRetroactiveStatus annualRetroactiveStatus;

}
