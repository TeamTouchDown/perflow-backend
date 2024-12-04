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

        Department foundDepartment = findDepartmentByDeptId(announcementRequestDTO.getDeptId());

        Employee foundEmployee = findEmployeeByEmpId(announcementRequestDTO.getEmpId());

        Announcement newAnnouncement = AnnouncementMapper.toEntity(announcementRequestDTO, foundDepartment, foundEmployee);

        announcementCommandRepository.save(newAnnouncement);
    }

    @Transactional
    public void updateAnnouncement(Long annId, AnnouncementRequestDTO announcementRequestDTO) {

        Announcement foundAnnouncement = findAnnouncementByAnnId(annId);

        findDepartmentByDeptId(announcementRequestDTO.getDeptId());

        findEmployeeByEmpId(announcementRequestDTO.getEmpId());

        if (isSameWriter(foundAnnouncement.getEmp().getEmpId(), announcementRequestDTO.getEmpId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        foundAnnouncement.updateAnnouncement(announcementRequestDTO);
    }

    @Transactional
    public void deleteAnnouncement(Long annId, String empId) {

        Announcement foundAnnouncement = findAnnouncementByAnnId(annId);

        if (isSameWriter(foundAnnouncement.getEmp().getEmpId(), empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        announcementCommandRepository.deleteById(annId);
    }

    private boolean isSameWriter(String empId, String requestEmpId) {

        return !empId.equals(requestEmpId);
    }

    private Announcement findAnnouncementByAnnId(Long annId) {

        return announcementCommandRepository.findById(annId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNOUNCEMENT));
    }

    private Department findDepartmentByDeptId(Long deptId) {

        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }

    private Employee findEmployeeByEmpId(String empId) {

        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
