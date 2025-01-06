package com.touchdown.perflowbackend.notification.command.mapper;

import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;

public class NotificationMessageMapper {

    public static NotificationMessageDTO toDTO(Notification notification) {

        return new NotificationMessageDTO(
                notification.getNotiId(),
                notification.getRefId(),
                notification.getRefType(),
                notification.getEmployee().getEmpId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getUrl(),
                notification.getCreateDatetime(),
                notification.getStatus(),
                notification.getRetryCount()
        );
    }
}
