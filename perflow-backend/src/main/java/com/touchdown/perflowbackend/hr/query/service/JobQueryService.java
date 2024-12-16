package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.employee.command.Mapper.EmployeeMapper;
import com.touchdown.perflowbackend.hr.command.application.mapper.JobMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;
import com.touchdown.perflowbackend.hr.query.repository.JobQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobQueryService {

    private final JobQueryRepository jobQueryRepository;

    public List<JobResponseDTO> getAllJobs() {

        List<Job> jobs = getJobList();

        return JobMapper.toJobResponseDTOList(jobs);
    }

    private List<Job> getJobList() {
        return jobQueryRepository.findAll();
    }
}
