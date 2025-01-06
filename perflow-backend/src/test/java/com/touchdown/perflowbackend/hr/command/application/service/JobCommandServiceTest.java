package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobUpdateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JobCommandServiceTest {

    @Mock
    private JobCommandRepository jobCommandRepository;

    @Mock
    private DepartmentCommandRepository departmentCommandRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JobCommandService jobCommandService;

    private JobCreateDTO createDTO;
    private Department mockDepartment;

    private JobUpdateDTO updateDTO;
    private Job existingJob;
    private Department existingDept;

    @BeforeEach
    void setUp() {
        // 테스트용 DTO
        createDTO = new JobCreateDTO();
        createDTO.setDeptId(10L);
        createDTO.setName("인사관리자");
        createDTO.setResponsibility("인사 관련 업무");


        mockDepartment = new Department();
        mockDepartment.setDepartmentId(10L);
        mockDepartment.setName("인사부");

        // 1) 테스트용 DTO
        updateDTO = new JobUpdateDTO();
        updateDTO.setJobId(100L);
        updateDTO.setDeptId(10L);
        updateDTO.setName("개발자");
        updateDTO.setResponsibility("개발 관련 업무");

        // 2) 기존 Job 엔티티
        existingJob = new Job();
        existingJob.setJobId(100L);
        existingJob.setName("기존 직무명");
        existingJob.setResponsibility("기존 담당 업무");

        // 3) 기존 Department
        existingDept = new Department();
        existingDept.setDepartmentId(10L);
        existingDept.setName("개발팀");
    }

    @Test
    @DisplayName("createJob - 정상적으로 Job 생성 시, persist & save 호출")
    void testCreateJob_success() {
        // given
        given(departmentCommandRepository.findById(createDTO.getDeptId()))
                .willReturn(Optional.of(mockDepartment));

        Job savedJob = new Job();
        savedJob.setJobId(100L);
        savedJob.setName("인사관리자");
        savedJob.setDept(mockDepartment);
        given(jobCommandRepository.save(any(Job.class)))
                .willReturn(savedJob);

        // when
        jobCommandService.createJob(createDTO);

        // then
        then(departmentCommandRepository).should(times(1))
                .findById(10L);

        then(entityManager).should(times(1))
                .persist(any(Job.class));

        then(jobCommandRepository).should(times(1))
                .save(any(Job.class));
    }

    @Test
    @DisplayName("createJob - 부서를 찾지 못하면 예외 발생")
    void testCreateJob_notFoundDepartment() {
        // given
        given(departmentCommandRepository.findById(createDTO.getDeptId()))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> jobCommandService.createJob(createDTO));

        // save나 persist 호출되지 않음
        then(entityManager).shouldHaveNoInteractions();
        then(jobCommandRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("updateJob - Job 엔티티를 찾지 못하면 예외 발생")
    void testUpdateJob_notFoundJob() {
        // given
        given(jobCommandRepository.findById(100L))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> jobCommandService.updateJob(updateDTO));
        // 예외 코드 확인 (optional)
        // assertEquals(ErrorCode.NOT_FOUND_JOB, ex.getErrorCode());

        // Department 조회, save 호출 모두 하지 않아야 함
        then(departmentCommandRepository).shouldHaveNoInteractions();
        then(jobCommandRepository).should(never()).save(any(Job.class));
    }

    @Test
    @DisplayName("updateJob - Department를 찾지 못하면 예외 발생")
    void testUpdateJob_notFoundDepartment() {
        // given
        // Job은 찾았으나
        given(jobCommandRepository.findById(100L))
                .willReturn(Optional.of(existingJob));
        // 부서는 못 찾음
        given(departmentCommandRepository.findById(10L))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> jobCommandService.updateJob(updateDTO));
        // 예외 코드 확인 (optional)
        // assertEquals(ErrorCode.NOT_FOUND_DEPARTMENT, ex.getErrorCode());

        // save(...)는 호출되지 않음
        then(jobCommandRepository).should(never()).save(any(Job.class));
    }
}