package com.touchdown.perflowbackend.notification.command.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final FcmService fcmService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveNotification(NotificationRequestDTO notificationRequestDTO) {
        try {
            fcmService.sendMessage(notificationRequestDTO.getTargetToken(), notificationRequestDTO.getContent(), notificationRequestDTO.getUrl());
            log.info("FCM 알림 전송 완료: {}", notificationRequestDTO);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패: {}", e.getMessage(), e);
        }
    }
}
