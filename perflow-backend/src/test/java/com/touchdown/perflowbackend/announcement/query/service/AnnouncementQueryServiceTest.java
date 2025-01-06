package com.touchdown.perflowbackend.announcement.query.service;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.query.dto.AnnouncementResponseDTO;
import com.touchdown.perflowbackend.announcement.query.repository.AnnouncementQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnnouncementQueryServiceTest {

    @Mock
    private AnnouncementQueryRepository announcementQueryRepository;

    @InjectMocks
    private AnnouncementQueryService announcementQueryService;

    private Announcement mockAnnouncement;
    private Department mockDepartment;
    private Employee mockEmployee;

    @BeforeEach
    void setUp() {
        DepartmentCreateDTO departmentCreateDTO = new DepartmentCreateDTO();
        departmentCreateDTO.setDepartmentId(1L);
        departmentCreateDTO.setName("Test Department");

        EmployeeCreateDTO employeeCreateDTO =
                EmployeeCreateDTO.builder()
                        .empId("EMP001")
                        .name("John Doe")
                        .build();

        // Mock Department
        mockDepartment = Department.builder()
                .createDTO(departmentCreateDTO)
                .build();

        // Mock Employee
        mockEmployee = Employee.builder()
                .registerDTO(employeeCreateDTO)
                .department(mockDepartment)
                .build();

        // Mock Announcement
        mockAnnouncement = Announcement.builder()
                .annId(1L)
                .dept(mockDepartment)
                .emp(mockEmployee)
                .title("Test Announcement")
                .content("This is a test announcement.")
                .build();
    }

    @Test
    @DisplayName("전체 공지 조회 성공")
    void testReadAll_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Announcement> mockPage = new PageImpl<>(Collections.singletonList(mockAnnouncement));
        given(announcementQueryRepository.findAll(pageable)).willReturn(mockPage);

        // when
        Page<AnnouncementResponseDTO> result = announcementQueryService.readAll(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Announcement", result.getContent().get(0).getTitle());
        verify(announcementQueryRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("단일 공지 조회 성공")
    void testReadOne_success() {
        // given
        given(announcementQueryRepository.findById(1L)).willReturn(Optional.of(mockAnnouncement));

        // when
        AnnouncementResponseDTO result = announcementQueryService.readOne(1L);

        // then
        assertEquals("Test Announcement", result.getTitle());
        verify(announcementQueryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("단일 공지 조회 실패 (공지 없음 예외)")
    void testReadOne_notFound() {
        // given
        given(announcementQueryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                announcementQueryService.readOne(1L));
        assertEquals(ErrorCode.NOT_FOUND_ANNOUNCEMENT, exception.getErrorCode());
        verify(announcementQueryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("제목으로 공지 검색 성공")
    void testSearchAnnouncementsByTitle_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Announcement> mockPage = new PageImpl<>(Collections.singletonList(mockAnnouncement));
        given(announcementQueryRepository.findByTitleContainingIgnoreCase("Test", pageable)).willReturn(mockPage);

        // when
        Page<AnnouncementResponseDTO> result = announcementQueryService.searchAnnouncementsByTitle("Test", pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Announcement", result.getContent().get(0).getTitle());
        verify(announcementQueryRepository, times(1)).findByTitleContainingIgnoreCase("Test", pageable);
    }

    @Test
    @DisplayName("부서 이름으로 공지 검색 성공")
    void testGetAnnouncementByDeptName_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Announcement> mockPage = new PageImpl<>(Collections.singletonList(mockAnnouncement));
        given(announcementQueryRepository.findByDeptName("Test Department", pageable)).willReturn(mockPage);

        // when
        Page<AnnouncementResponseDTO> result = announcementQueryService.getAnnouncementByDeptName("Test Department", pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Announcement", result.getContent().get(0).getTitle());
        verify(announcementQueryRepository, times(1)).findByDeptName("Test Department", pageable);
    }
}
