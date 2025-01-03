package com.touchdown.perflowbackend.notification.query.mapper;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.query.dto.NotificationResponseDTO;

public class NotificationMapper {

    public static NotificationResponseDTO toDTO(Notification notification) {

        return new NotificationResponseDTO(
                notification.getNotiId(),
                notification.getRefId(),
                notification.getRefType().toString(),
                notification.getEmployee().getEmpId(),
                notification.getContent(),
                notification.getUrl(),
                notification.getCreateDatetime()
        );
    }
}
