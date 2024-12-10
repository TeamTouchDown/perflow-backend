package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineListResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocQueryService {

    private final ApproveLineQueryRepository approveLineQueryRepository;

    @Transactional
    public Page<MyApproveLineListResponseDTO> getApproveLineList(
            Pageable pageable,
            String createUserId,
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        return approveLineQueryRepository.findAllMyApproveLines(pageable, createUserId, name, startDate, endDate)
                .map(DocMapper::toMyApproveLineResponseDTO);
    }
}
