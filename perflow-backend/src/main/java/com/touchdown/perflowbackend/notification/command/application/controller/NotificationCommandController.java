package com.touchdown.perflowbackend.notification.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.notification.command.application.service.NotificationCommandService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationCommandService notificationCommandService;

    @DeleteMapping("/{notiId}")
    public ResponseEntity<SuccessCode> deleteNotification(@PathVariable("notiId") Long notiId) {

        notificationCommandService.deleteNotification(notiId, EmployeeUtil.getEmpId());

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessCode> deleteAllNotifications() {
        notificationCommandService.deleteAllNotifications(EmployeeUtil.getEmpId());

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
