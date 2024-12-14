package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocQueryService {

    private final ApproveLineQueryRepository approveLineQueryRepository;
    private final DocQueryRepository docQueryRepository;

    // 나의 결재선 목록 조회
    @Transactional(readOnly = true)
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

    // 나의 결재선 상세 조회
    @Transactional(readOnly = true)
    public MyApproveLineDetailResponseDTO getOneMyApproveLine(Long groupId) {

        List<ApproveLine> lines = approveLineQueryRepository.findByGroupId(groupId);

        if (lines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE);
        }

        return DocMapper.toMyApproveLineDetailResponseDTO(lines);
    }

    // 대기 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<WaitingDocListResponseDTO> getWaitingDocList(Pageable pageable, String empId) {

        // doc id 페이징 처리
        Page<Doc> docs = docQueryRepository.findWaitingDocsByUser(empId, pageable);

        return docs.map(DocMapper::toWaitingDocListResponseDTO);
    }
//
//    @Transactional(readOnly = true)
//    public WaitingDocDetailResponseDTO getOneWaitingDoc(Long docId) {
//
//        Doc doc = docQueryRepository.findDocDetailsById(docId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));
//
//        return DocMapper.toWaitingDocDetailResponseDTO(doc);
//    }
}
