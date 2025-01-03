package com.touchdown.perflowbackend.notification.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationQueryRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByEmployee(Employee employee);
}
