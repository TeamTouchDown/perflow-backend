package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.NotificationStatus;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import com.touchdown.perflowbackend.notification.command.mapper.NotificationMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
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
        log.info("ResendPendingNotifications 호출됨 for empId: {}", empId);

        List<Notification> pendingList = notificationCommandRepository
                .findByEmployeeEmpIdAndStatusIn(empId, Arrays.asList(NotificationStatus.WAITING, NotificationStatus.FAILED));

        log.info("찾은 대기 중인 알림 개수: {}", pendingList.size());

        if (pendingList.isEmpty()) {
            log.info("대기 중인 알림이 없음.");
            return;
        }

        // 토큰 조회 (로그인 직후면 토큰이 생겼을 것)
        List<String> tokens = fcmTokenRepository.findByEmployeeEmpId(empId)
                .stream()
                .map(FcmToken::getFcmToken)
                .toList();

        log.info("조회된 FCM 토큰 개수: {}", tokens.size());

        for (Notification noti : pendingList) {
            log.info("처리할 알림 ID: {}, 현재 retryCount: {}", noti.getNotiId(), noti.getRetryCount());

            if (noti.getRetryCount() >= MAX_RETRY) {
                log.warn("알림 ID: {}는 이미 최대 재시도 횟수를 초과하여 건너뜀.", noti.getNotiId());
                continue;
            }

            if (tokens.isEmpty()) {
                log.warn("알림 ID: {}에 대한 토큰이 없음. 상태를 WAITING으로 유지.", noti.getNotiId());
                noti.updateStatus(NotificationStatus.WAITING);
                noti.updateRetryCount(noti.getRetryCount() + 1);
                continue;
            }

            NotificationMessageDTO dto = NotificationMessageMapper.toDTO(noti);
            log.info("알림 ID: {}에 대해 FCM 전송 시도: {}", noti.getNotiId(), dto);

            try {
                fcmService.sendMessages(tokens, dto);
                noti.updateStatus(NotificationStatus.SUCCESS);
                log.info("알림 ID: {}의 FCM 전송 성공.", noti.getNotiId());
            } catch (Exception e) {
                noti.updateStatus(NotificationStatus.FAILED);
                noti.updateRetryCount(noti.getRetryCount() + 1);
                log.error("알림 ID: {}의 FCM 전송 실패. 에러: {}", noti.getNotiId(), e.getMessage(), e);
            }
        }
        notificationCommandRepository.saveAll(pendingList);
        log.info("resendPendingNotifications 처리 완료 for empId: {}", empId);
    }
}
