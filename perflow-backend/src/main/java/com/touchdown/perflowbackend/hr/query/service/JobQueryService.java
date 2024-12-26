package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.application.mapper.JobMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.JobResponseListDTO;
import com.touchdown.perflowbackend.hr.query.repository.JobQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobQueryService {

    private final JobQueryRepository jobQueryRepository;

    public JobResponseListDTO getAllJobs(Pageable pageable) {

        Page<Job> pages = getJobList(pageable);

        List<JobResponseDTO> jobResponseDTO = JobMapper.toJobResponseDTOList(pages.getContent());

        return JobResponseListDTO.builder()
                .jobResponseDTOList(jobResponseDTO)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }

    private Page<Job> getJobList(Pageable pageable) {

        return jobQueryRepository.findAll(pageable);
    }
}
