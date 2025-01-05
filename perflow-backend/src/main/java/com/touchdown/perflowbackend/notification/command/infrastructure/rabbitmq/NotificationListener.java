package com.touchdown.perflowbackend.notification.command.infrastructure.rabbitmq;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.application.service.FcmService;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.NotificationStatus;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final FcmService fcmService;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationCommandRepository notificationCommandRepository;

    private static final int MAX_RETRY = 3;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveNotification(
            NotificationMessageDTO notificationMessageDTO,
            @Header(name = "x-death", required = false) Map<?, ?> xDeathHeader
            ) {

        log.info("받은 알림: {}", notificationMessageDTO);

        Notification notification = notificationCommandRepository.findById(notificationMessageDTO.getNotiId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTI));

        int currentRetryCount = notification.getRetryCount();

        List<String> tokens = fcmTokenRepository.findByEmployeeEmpId(notificationMessageDTO.getEmpId())
                .stream()
                .map(FcmToken::getFcmToken)
                .toList();

        if (tokens.isEmpty()) {

            // 토큰 없음 → 어차피 지금은 보낼 수 없음
            // "로그인 시점에" 다시 시도하도록 WAITING 상태 유지
            notification.updateStatus(NotificationStatus.WAITING);
            // retryCount 증가(선택적): token이 없어도 1회 시도한 거로 칠 수 있으면 ++
            notification.updateRetryCount(currentRetryCount + 1);

            notificationCommandRepository.save(notification);

            throw new AmqpRejectAndDontRequeueException(ErrorCode.NOT_EXIST_FCM_TOKEN.getMessage());
        }

        try {
            fcmService.sendMessages(tokens, notificationMessageDTO);

            notification.updateStatus(NotificationStatus.SUCCESS);
            notificationCommandRepository.save(notification);

            log.info("FCM 알림 전송 완료: {}", notificationMessageDTO);
        } catch (Exception e) {
            log.error("FCM 전송 실패 -> DLQ로 보내기. error={}", e.getMessage(), e);
            // 재시도 횟수 증가
            notification.updateRetryCount(currentRetryCount + 1);

            if (notification.getRetryCount() >= MAX_RETRY) {
                // 자동 재시도 한계 초과 → 영구 실패
                notification.updateStatus(NotificationStatus.FAILED);
                notificationCommandRepository.save(notification);
                log.warn("FCM 재시도 한계 초과 -> 영구 FAILED 처리: {}", notificationMessageDTO);
                // 여기서 return or 그냥 ack 처리하면 MQ에서는 메시지가 사라짐.
                // → throw 안 던지면 ACK 처리됨.
                return;

            } else {
                // 아직 재시도 가능 횟수 남음 → 상태는 FAILED로 갱신
                notification.updateStatus(NotificationStatus.FAILED);
                notificationCommandRepository.save(notification);

                // DLQ로 이동시키기 위해 예외 던짐
                throw new AmqpRejectAndDontRequeueException(ErrorCode.NOTIFICATION_SEND_FAILED.getMessage());
            }
        }
    }
}
