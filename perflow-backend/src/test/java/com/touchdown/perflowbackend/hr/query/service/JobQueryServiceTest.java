package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseListDTO;
import com.touchdown.perflowbackend.hr.query.repository.JobQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JobQueryServiceTest {

    @Mock
    private JobQueryRepository jobQueryRepository;

    @InjectMocks
    private JobQueryService jobQueryService;

    private Pageable pageable;
    private List<Job> mockJobList;

    @BeforeEach
    void setUp() {
        // Pageable 설정 (page=0, size=5, 정렬)
        pageable = PageRequest.of(0, 5, Sort.by("jobId").ascending());

        // 테스트용 Job 리스트
        mockJobList = new ArrayList<>();
        Department mockDept = mock(Department.class);
        Job j1 = new Job();
        j1.setJobId(100L);
        j1.setName("인사관리자");
        j1.setResponsibility("인사 관련 업무");
        j1.setDept(mockDept);

        Job j2 = new Job();
        j2.setJobId(200L);
        j2.setName("재무관리자");
        j2.setResponsibility("재무 관련 업무");
        j2.setDept(mockDept);

        mockJobList.add(j1);
        mockJobList.add(j2);
    }

    @Test
    @DisplayName("getAllJobs - 정상적으로 페이징 조회 시, DTO 리스트 및 페이징 정보 반환")
    void testGetAllJobs_success() {
        // given
        Page<Job> mockPage = new PageImpl<>(mockJobList, pageable, mockJobList.size());
        given(jobQueryRepository.findAll(pageable)).willReturn(mockPage);

        // when
        JobResponseListDTO result = jobQueryService.getAllJobs(pageable);

        // then
        then(jobQueryRepository).should(times(1)).findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getJobResponseDTOList().size()); // mockJobList.size()

        JobResponseDTO firstDto = result.getJobResponseDTOList().get(0);
        assertEquals("인사관리자", firstDto.getName());
        assertEquals("인사 관련 업무", firstDto.getResponsibility());

        // 페이징 정보 검증
        assertEquals(1, result.getTotalPages());    // 총 2개 / size=5 → 페이지 수=1
        assertEquals(2, result.getTotalItems());    // 총 2개
        assertEquals(1, result.getCurrentPage());   // 0-based page → 1-based 반환
        assertEquals(5, result.getPageSize());      // size=5
    }

    @Test
    @DisplayName("getAllJobs - 데이터가 없으면 빈 리스트와 적절한 페이징 정보 반환")
    void testGetAllJobs_empty() {
        // given
        List<Job> emptyList = new ArrayList<>();
        Page<Job> emptyPage = new PageImpl<>(emptyList, pageable, 0);
        given(jobQueryRepository.findAll(pageable)).willReturn(emptyPage);

        // when
        JobResponseListDTO result = jobQueryService.getAllJobs(pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getJobResponseDTOList().isEmpty());
        assertEquals(1, result.getCurrentPage());
        assertEquals(0, result.getTotalItems());
        assertEquals(0, result.getTotalPages());
    }
}