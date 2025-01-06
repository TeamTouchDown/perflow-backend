package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.config.RabbitMQConfig;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.notification.command.application.dto.NotificationMessageDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.Notification;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.NotificationStatus;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.RefType;
import com.touchdown.perflowbackend.notification.command.domain.repository.NotificationCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final NotificationCommandRepository notificationCommandRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EmployeeCommandRepository employeeCommandRepository;

    /**
     * 도메인 이벤트 처리 & 알림 발행
     */
    public void createAndPublishNotification(
            Long refId,
            String refType,
            String empId,
            String title,
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
                .title(title)
                .content(content)
                .url(url)
                .status(NotificationStatus.WAITING)
                .retryCount(0)
                .build();

        notificationCommandRepository.save(notification);

        // 2. NotificationMessageDTO 변환
        NotificationMessageDTO dto = NotificationMessageDTO.builder()
                .notiId(notification.getNotiId())
                .refId(notification.getRefId())
                .refType(notification.getRefType())
                .empId(employee.getEmpId())
                .title(notification.getTitle())
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
    }

    @Transactional
    public void deleteNotification(Long notiId, String empId) {

        Notification notification = notificationCommandRepository.findById(notiId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTI));

        if (notification.getEmployee().getEmpId().equals(empId)) {
            notificationCommandRepository.deleteById(notiId);
        }
    }

    @Transactional
    public void deleteAllNotifications(String empId) {
        notificationCommandRepository.deleteAllByEmployeeEmpId(empId);
    }
}
