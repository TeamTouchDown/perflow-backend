package com.touchdown.perflowbackend.notification.command.application.dto;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.RefType;
import lombok.Data;

@Data
public class NotificationRequestDTO {

    private Long refId;

    private RefType refType;

    private String content;

    private String url;

    private String targetToken;
}
