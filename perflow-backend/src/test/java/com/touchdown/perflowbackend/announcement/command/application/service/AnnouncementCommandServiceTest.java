package com.touchdown.perflowbackend.announcement.command.application.service;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.command.domain.repository.AnnouncementCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.file.command.application.service.FileService;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementCommandServiceTest {

    @Mock
    private AnnouncementCommandRepository announcementCommandRepository;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @Mock
    private DepartmentCommandRepository departmentCommandRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private AnnouncementCommandService announcementCommandService;

    private AnnouncementRequestDTO requestDTO;
    private Employee mockEmployee;
    private Department mockDepartment;
    private Announcement mockAnnouncement;

    @BeforeEach
    void setUp() {
        // 요청 DTO
        requestDTO = new AnnouncementRequestDTO();
        requestDTO.setDeptId(1L);
        requestDTO.setTitle("Test Announcement");
        requestDTO.setContent("This is a test announcement.");

        DepartmentCreateDTO departmentCreateDTO = new DepartmentCreateDTO();
        departmentCreateDTO.setDepartmentId(1L);
        departmentCreateDTO.setName("Test Department");

        EmployeeCreateDTO employeeCreateDTO =
                EmployeeCreateDTO.builder()
                        .empId("EMP001")
                        .build();

        // Mock 객체 초기화
        mockDepartment = Department.builder()
                .createDTO(departmentCreateDTO)
                .build();

        mockEmployee = Employee.builder()
                .registerDTO(employeeCreateDTO)
                .department(mockDepartment)
                .build();

        mockAnnouncement = Announcement.builder()
                .annId(1L)
                .dept(mockDepartment)
                .emp(mockEmployee)
                .title("Test Announcement")
                .content("This is a test announcement.")
                .build();
    }

    @Test
    @DisplayName("공지 생성 성공")
    void testCreateAnnouncement_success() {
        // given
        given(departmentCommandRepository.findById(requestDTO.getDeptId())).willReturn(Optional.of(mockDepartment));
        given(employeeCommandRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
        given(announcementCommandRepository.save(any(Announcement.class))).willReturn(mockAnnouncement);

        List<MultipartFile> files = Collections.emptyList();

        // when
        announcementCommandService.createAnnouncement("EMP001", requestDTO, files);

        // then
        verify(departmentCommandRepository, times(1)).findById(1L);
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(announcementCommandRepository, times(1)).save(any(Announcement.class));
        verify(fileService, never()).uploadFiles(any(), any(), any());
    }

    @Test
    @DisplayName("공지 생성 부서 불일치 예외")
    void testCreateAnnouncement_departmentMismatch() {
        // given
        DepartmentCreateDTO deptDTO = new DepartmentCreateDTO();
        deptDTO.setDepartmentId(2L);
        deptDTO.setName("Other Department");

        EmployeeCreateDTO empDTO =
                EmployeeCreateDTO.builder()
                        .empId("EMP001")
                        .build();

        Department otherDepartment = Department.builder()
                .createDTO(deptDTO)
                .build();

        mockEmployee = Employee.builder()
                .registerDTO(empDTO)
                .department(otherDepartment)
                .build();

        given(departmentCommandRepository.findById(requestDTO.getDeptId())).willReturn(Optional.of(mockDepartment));
        given(employeeCommandRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                announcementCommandService.createAnnouncement("EMP001", requestDTO, Collections.emptyList()));

        assertEquals(ErrorCode.NOT_MATCH_DEPARTMENT, exception.getErrorCode());
        verify(announcementCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("공지 삭제 성공")
    void testDeleteAnnouncement_success() {
        // given
        given(announcementCommandRepository.findById(1L)).willReturn(Optional.of(mockAnnouncement));

        // when
        announcementCommandService.deleteAnnouncement(1L, "EMP001");

        // then
        verify(announcementCommandRepository, times(1)).deleteById(1L);
        verify(fileService, times(1)).deleteFilesByDomainEntity(mockAnnouncement);
    }

    @Test
    @DisplayName("공지 삭제 작성자 불일치 예외")
    void testDeleteAnnouncement_writerMismatch() {
        // given
        given(announcementCommandRepository.findById(1L)).willReturn(Optional.of(mockAnnouncement));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                announcementCommandService.deleteAnnouncement(1L, "EMP002"));

        assertEquals(ErrorCode.NOT_MATCH_WRITER, exception.getErrorCode());
        verify(announcementCommandRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("공지 수정 성공")
    void testUpdateAnnouncement_success() {
        // given
        given(announcementCommandRepository.findById(1L)).willReturn(Optional.of(mockAnnouncement));
        given(departmentCommandRepository.findById(requestDTO.getDeptId())).willReturn(Optional.of(mockDepartment));
        given(employeeCommandRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));

        List<MultipartFile> addedFiles = Collections.emptyList();
        List<Long> deletedFileIds = Collections.emptyList();

        // when
        announcementCommandService.updateAnnouncement(1L, "EMP001", requestDTO, addedFiles, deletedFileIds);

        // then
        verify(announcementCommandRepository, times(1)).findById(1L);
        verify(fileService, never()).deleteFile(any());
        verify(fileService, never()).uploadFiles(any(), any(), any());
    }
}
