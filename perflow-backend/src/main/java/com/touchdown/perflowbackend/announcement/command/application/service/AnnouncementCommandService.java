package com.touchdown.perflowbackend.announcement.command.application.service;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.command.domain.repository.AnnouncementCommandRepository;
import com.touchdown.perflowbackend.announcement.command.mapper.AnnouncementMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.file.command.application.dto.FileUploadDTO;
import com.touchdown.perflowbackend.file.command.application.service.FileService;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementCommandService {

    private final AnnouncementCommandRepository announcementCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final FileService fileService;

    @Transactional
    public void createAnnouncement(String empId, AnnouncementRequestDTO announcementRequestDTO, List<MultipartFile> files) {

        Department foundDepartment = findDepartmentByDeptId(announcementRequestDTO.getDeptId());

        Employee foundEmployee = findEmployeeByEmpId(empId);

        if (!isSameDept(foundDepartment, foundEmployee)) {
            throw new CustomException(ErrorCode.NOT_MATCH_DEPARTMENT);
        }

        Announcement newAnnouncement = AnnouncementMapper.toEntity(announcementRequestDTO, foundDepartment, foundEmployee);

        announcementCommandRepository.save(newAnnouncement);

        fileUpload(files, newAnnouncement);
    }

    @Transactional
    public void updateAnnouncement(Long annId, String empId, AnnouncementRequestDTO announcementRequestDTO, List<MultipartFile> addedFiles, List<Long> deletedFileIds) {
        Announcement foundAnnouncement = findAnnouncementByAnnId(annId);

        findDepartmentByDeptId(announcementRequestDTO.getDeptId());
        findEmployeeByEmpId(empId);

        if (isSameWriter(foundAnnouncement.getEmp().getEmpId(), empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        foundAnnouncement.updateAnnouncement(announcementRequestDTO);

        // 삭제할 파일 처리
        if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
            for (Long fileId : deletedFileIds) {
                File file = fileService.findFileById(fileId);
                fileService.deleteFile(file);
            }
        }

        // 추가할 파일 처리
        if (addedFiles != null && !addedFiles.isEmpty()) {
            fileUpload(addedFiles, foundAnnouncement);
        }
    }

    @Transactional
    public void deleteAnnouncement(Long annId, String empId) {
        Announcement foundAnnouncement = findAnnouncementByAnnId(annId);

        if (isSameWriter(foundAnnouncement.getEmp().getEmpId(), empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        // 연관된 파일 모두 삭제
        fileService.deleteFilesByDomainEntity(foundAnnouncement);

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

    private boolean isSameDept(Department foundDepartment, Employee foundEmployee) {
        return foundDepartment.getName().equals(foundEmployee.getDept().getName());
    }

    private void fileUpload(List<MultipartFile> files, Announcement newAnnouncement) {
        if (files != null && !files.isEmpty()) {
            List<FileUploadDTO> uploadedFiles = fileService.uploadFiles(files, FileDirectory.ANNOUNCEMENT, newAnnouncement);

            // 필요 시 업로드된 파일 정보를 출력하거나 추가 작업 수행 가능
            uploadedFiles.forEach(file -> log.info("Uploaded file: {}", file.getFileName()));
        }
    }
}
