package com.touchdown.perflowbackend.notification.command.domain.repository;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;

public interface NotificationCommandRepository {

    Notification save(Notification notification);
}
