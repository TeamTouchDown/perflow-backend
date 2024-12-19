package com.touchdown.perflowbackend.notification.command.infrastructure;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long>, NotificationCommandRepository {
}
