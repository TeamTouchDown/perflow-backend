package com.touchdown.perflowbackend.announcement.command.application.service;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.command.domain.repository.AnnouncementCommandRepository;
import com.touchdown.perflowbackend.announcement.command.mapper.AnnouncementMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementCommandService {

    private final AnnouncementCommandRepository announcementCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;

    @Transactional
    public void createAnnouncement(AnnouncementRequestDTO announcementRequestDTO) {

        Department foundDepartment = departmentCommandRepository.findById(announcementRequestDTO.getDeptId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));

        Employee foundEmployee = employeeCommandRepository.findById(announcementRequestDTO.getEmpId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        Announcement newAnnouncement = AnnouncementMapper.toEntity(announcementRequestDTO, foundDepartment, foundEmployee);

        announcementCommandRepository.save(newAnnouncement);
    }
}
