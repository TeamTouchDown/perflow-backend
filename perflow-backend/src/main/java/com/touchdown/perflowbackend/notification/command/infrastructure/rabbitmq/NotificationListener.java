package com.touchdown.perflowbackend.notification.command.infrastructure.rabbitmq;

import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.application.service.FcmService;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final FcmService fcmService;
    private final FcmTokenRepository fcmTokenRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveNotification(NotificationMessageDTO notificationMessageDTO) {

        log.info("받은 알림: {}", notificationMessageDTO);

        List<String> tokens = fcmTokenRepository.findByEmployeeEmpId(notificationMessageDTO.getEmpId())
                .stream()
                .map(FcmToken::getFcmToken)
                .toList();

        if (tokens.isEmpty()) {
            log.warn("해당 사원ID에 대한 토큰 없음: {}", notificationMessageDTO.getEmpId());

            return;
        }

        try {
            fcmService.sendMessages(tokens, notificationMessageDTO);
            log.info("FCM 알림 전송 완료: {}", notificationMessageDTO);
        } catch (Exception e) {
            log.error("FCM 전송 실패: {}", e.getMessage(), e);
        }
    }
}
