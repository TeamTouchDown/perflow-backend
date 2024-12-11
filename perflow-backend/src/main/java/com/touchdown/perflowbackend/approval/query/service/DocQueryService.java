package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocQueryService {

    private final ApproveLineQueryRepository approveLineQueryRepository;

    @Transactional
    public Page<MyApproveLineGroupResponseDTO> getMyApproveLineList(Pageable pageable, String createUserId
    ) {

        return approveLineQueryRepository.findAllMyApproveLines(pageable, createUserId)
                .map(group -> {
                    List<ApproveLine> lines = approveLineQueryRepository.findByGroupId(group.getGroupId());
                    return DocMapper.toMyApproveLineGroupResponseDTO(
                            group.getGroupId(),
                            group.getName(),
                            group.getDescription(),
                            group.getCreateDatetime(),
                            lines
                    );
                });
    }

    @Transactional
    public MyApproveLineDetailResponseDTO getOneMyApproveLine(Long groupId) {

        List<ApproveLine> lines = approveLineQueryRepository.findByGroupId(groupId);

        if (lines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE);
        }

        return DocMapper.toMyApproveLineDetailResponseDTO(lines);
    }
}
