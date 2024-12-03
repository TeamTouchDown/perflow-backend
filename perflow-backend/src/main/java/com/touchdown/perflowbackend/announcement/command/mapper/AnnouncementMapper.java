package com.touchdown.perflowbackend.announcement.command.mapper;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.department.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

public class AnnouncementMapper {

    public static Announcement toEntity(AnnouncementRequestDTO announcementRequestDTO, Department department, Employee employee) {

        return Announcement.builder()
                .dept(department)
                .emp(employee)
                .title(announcementRequestDTO.getTitle())
                .content(announcementRequestDTO.getContent())
                .build();
    }
}
