package com.touchdown.perflowbackend.notification.command.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;

    /**
     * 여러 토큰으로 메시지 전송
     */
    public void sendMessages(List<String> targetTokens, NotificationMessageDTO notificationMessageDTO) {
        Notification notification = Notification
                .builder()
                .setTitle("알림 도착!")
                .setBody(notificationMessageDTO.getContent())
                .build();

        for (String token : targetTokens) {
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(token)
                    .putData("click_action", notificationMessageDTO.getUrl())
                    .build();

            try {
                String response = firebaseMessaging.send(message);
                log.info("FCM 전송 성공. response={}, token={}", response, token);
            } catch (FirebaseMessagingException e) {
                log.error("FCM 전송 실패. error={}, token={}", e.getMessage(), token, e);
                // 필요 시, 토큰 삭제 또는 재시도 로직 추가
            }
        }
    }
}
