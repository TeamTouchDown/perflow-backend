package com.touchdown.perflowbackend.announcement.command.application.dto;

import lombok.Data;

@Data
public class AnnouncementRequestDTO {

    private Long deptId;

    private String title;

    private String content;
}
