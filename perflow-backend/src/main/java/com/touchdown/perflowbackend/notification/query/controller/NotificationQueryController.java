package com.touchdown.perflowbackend.notification.query.controller;

import com.touchdown.perflowbackend.notification.query.dto.NotificationResponseDTO;
import com.touchdown.perflowbackend.notification.query.service.NotificationQueryService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {

        return ResponseEntity.ok(notificationQueryService.getNotifications(EmployeeUtil.getEmpId()));
    }
}
