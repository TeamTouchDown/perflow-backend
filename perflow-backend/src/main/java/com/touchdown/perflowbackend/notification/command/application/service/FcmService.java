package com.touchdown.perflowbackend.notification.command.application.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseApp firebaseApp;

    public void sendMessage(String targetToken, String content, String url) throws FirebaseMessagingException {
        if (targetToken == null || targetToken.isEmpty()) {
            return;
        }

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle("알림")
                        .setBody(content)
                        .build())
                .putData("url", url) // 알림 클릭 시 이동할 URL
                .build();

        FirebaseMessaging.getInstance(firebaseApp).send(message);
    }
}
