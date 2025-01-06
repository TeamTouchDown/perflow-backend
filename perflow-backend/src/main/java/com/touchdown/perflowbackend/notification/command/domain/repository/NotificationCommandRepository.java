package com.touchdown.perflowbackend.notification.command.domain.repository;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationCommandRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEmployeeEmpIdAndStatusIn(String employeeEmpId, List<NotificationStatus> status);
}
