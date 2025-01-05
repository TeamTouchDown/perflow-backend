package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.NotificationStatus;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import com.touchdown.perflowbackend.notification.command.mapper.NotificationMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationResendService {

    private final NotificationCommandRepository notificationCommandRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    private static final int MAX_RETRY = 3;

    /**
     * 사용자 로그인 직후 호출.
     * WAITING/FAILED 알림들 중 retryCount < MAX_RETRY 인 알림을 즉시 재전송 시도.
     */
    @Transactional
    public void resendPendingNotifications(String empId) {
        List<Notification> pendingList = notificationCommandRepository
                .findByEmployeeEmpIdAndStatusIn(empId, Arrays.asList(NotificationStatus.WAITING, NotificationStatus.FAILED));

        if (pendingList.isEmpty()) {
            return;
        }

        // 토큰 조회 (로그인 직후면 토큰이 생겼을 것)
        List<String> tokens = fcmTokenRepository.findByEmployeeEmpId(empId)
                .stream()
                .map(FcmToken::getFcmToken)
                .toList();

        for (Notification noti : pendingList) {
            if (noti.getRetryCount() >= MAX_RETRY) {
                // 이미 재시도 한계
                continue;
            }

            if (tokens.isEmpty()) {
                // 여전히 토큰 없다면 어쩔 수 없음
                noti.updateStatus(NotificationStatus.WAITING);
                noti.updateRetryCount(noti.getRetryCount() + 1);
                continue;
            }

            NotificationMessageDTO dto = NotificationMessageMapper.toDTO(noti);

            try {
                fcmService.sendMessages(tokens, dto);
                noti.updateStatus(NotificationStatus.SUCCESS);
                // 성공하면 retryCount 유지 (혹은 그대로)
            } catch (Exception e) {
                noti.updateStatus(NotificationStatus.FAILED);
                noti.updateRetryCount(noti.getRetryCount() + 1);
            }
        }
        notificationCommandRepository.saveAll(pendingList);
    }
}
