package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(NotificationRequestDTO notificationRequestDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, notificationRequestDTO);
    }
}
