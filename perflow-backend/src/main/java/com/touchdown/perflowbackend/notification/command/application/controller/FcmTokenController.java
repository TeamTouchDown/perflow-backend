package com.touchdown.perflowbackend.notification.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.notification.command.application.dto.FcmTokenRequestDTO;
import com.touchdown.perflowbackend.notification.command.application.service.FcmTokenService;
import com.touchdown.perflowbackend.notification.command.application.service.NotificationResendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fcm/token")
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;
    private final NotificationResendService notificationResendService;

    @PostMapping
    public ResponseEntity<SuccessCode> registerToken(@RequestBody FcmTokenRequestDTO fcmTokenRequestDTO) {

        fcmTokenService.registerFcmToken(fcmTokenRequestDTO);

        notificationResendService.resendPendingNotifications(fcmTokenRequestDTO.getEmpId());

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<SuccessCode> deleteToken(@RequestBody FcmTokenRequestDTO fcmTokenRequestDTO) {

        fcmTokenService.deleteFcmToken(fcmTokenRequestDTO.getToken());

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
