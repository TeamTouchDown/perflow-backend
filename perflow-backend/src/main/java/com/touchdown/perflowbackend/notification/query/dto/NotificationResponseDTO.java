package com.touchdown.perflowbackend.notification.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long notiId;

    private Long refId;

    private String refType;

    private String empId;

    private String content;

    private String url;

    private LocalDateTime createDatetime;

    public static Comparator<NotificationResponseDTO> byCreateDatetimeDesc =
            Comparator.comparing(NotificationResponseDTO::getCreateDatetime).reversed();
}
