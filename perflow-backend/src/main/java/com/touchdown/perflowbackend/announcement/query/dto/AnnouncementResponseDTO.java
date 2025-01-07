package com.touchdown.perflowbackend.announcement.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnnouncementResponseDTO {

    private Long annId;

    private String deptName;

    private String empId;

    private String empName;

    private String title;

    private String content;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;
}
