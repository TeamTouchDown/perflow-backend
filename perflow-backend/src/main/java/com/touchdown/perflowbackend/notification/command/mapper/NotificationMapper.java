package com.touchdown.perflowbackend.notification.command.mapper;

import com.touchdown.perflowbackend.notification.command.application.dto.NotificationRequestDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;

public class NotificationMapper {

    public static Notification toEntity(NotificationRequestDTO notificationRequestDTO) {

        return Notification.builder()
                .refId(notificationRequestDTO.getRefId())
                .refType(notificationRequestDTO.getRefType())
                .content(notificationRequestDTO.getContent())
                .url(notificationRequestDTO.getUrl())
                .targetToken(notificationRequestDTO.getTargetToken())
                .build();
    }
}
