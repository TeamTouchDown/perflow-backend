package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.notification.command.application.dto.NotificationRequestDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import com.touchdown.perflowbackend.notification.command.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final NotificationCommandRepository notificationCommandRepository;
    private final NotificationProducer notificationProducer;

    public void sendNotification(NotificationRequestDTO notificationRequestDTO) {

        Notification notification = NotificationMapper.toEntity(notificationRequestDTO);

        notificationCommandRepository.save(notification);

        notificationProducer.send(notificationRequestDTO);
    }
}
