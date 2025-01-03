package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.JobMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobCommandService {

    private final JobCommandRepository jobCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createJob(JobCreateDTO jobCreateDTO) {

        Department department = getDepartment(jobCreateDTO.getDeptId());

        Job job = JobMapper.toEntity(jobCreateDTO, department);

        entityManager.persist(job);

        jobCommandRepository.save(job);

    }

    @Transactional
    public void updateJob(JobUpdateDTO jobUpdateDTO) {

        Job job = getJob(jobUpdateDTO.getJobId());

        Department department = getDepartment(jobUpdateDTO.getDeptId());

        job.updateJob(jobUpdateDTO, department);

        jobCommandRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {

        Job job = getJob(jobId);

        jobCommandRepository.delete(job);
    }

    private Job getJob(Long jobId) {
        return jobCommandRepository.findById(jobId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_JOB)
        );
    }

    public Department getDepartment(Long deptId) {

        return departmentCommandRepository.findById(deptId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT)
        );
    }
}
