package com.touchdown.perflowbackend.notification.command.application.dto;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.RefType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessageDTO {

    private Long notiId;

    private Long refId;

    private RefType refType;

    private String empId;

    private String content;

    private String url;

    private LocalDateTime createDatetime;
}
