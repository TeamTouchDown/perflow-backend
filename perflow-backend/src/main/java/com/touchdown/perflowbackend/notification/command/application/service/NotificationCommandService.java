package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.RefType;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final NotificationCommandRepository notificationCommandRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    /**
     * 도메인 이벤트 처리 & 알림 발행
     */
    public void createAndPublishNotification(
            Long refId,
            String refType,
            String empId,
            String content,
            String url
    ) {

        Employee employee = employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        // 1. Notification 엔티티 생성 & DB 저장
        Notification notification = Notification.builder()
                .refId(refId)
                .refType(RefType.valueOf(refType))
                .employee(employee)
                .content(content)
                .url(url)
                .build();

        notificationCommandRepository.save(notification);

        // 2. NotificationMessageDTO 변환
        NotificationMessageDTO dto = NotificationMessageDTO.builder()
                .notiId(notification.getNotiId())
                .refId(notification.getRefId())
                .refType(notification.getRefType())
                .empId(employee.getEmpId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .createDatetime(notification.getCreateDatetime())
                .build();

        // 3. RabbitMQ 발행
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                dto
        );

        // 4. FCM 발송
        List<String> fcmTokens = fcmTokenRepository.findByEmployeeEmpId(employee.getEmpId())
                .stream().map(FcmToken::getFcmToken)
                .toList();

        fcmService.sendMessages(fcmTokens, dto);
    }
}
