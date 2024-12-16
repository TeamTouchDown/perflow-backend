package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocField;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocFieldQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocQueryService {

    private final ApproveLineQueryRepository approveLineQueryRepository;
    private final DocQueryRepository docQueryRepository;
    private final DocFieldQueryRepository docFieldQueryRepository;

    // 나의 결재선 목록 조회
    @Transactional(readOnly = true)
    public Page<MyApproveLineGroupResponseDTO> getMyApproveLineList(Pageable pageable, String createUserId
    ) {

        return approveLineQueryRepository.findAllMyApproveLines(pageable, createUserId);
    }

    // 나의 결재선 상세 조회
    @Transactional(readOnly = true)
    public MyApproveLineDetailResponseDTO getOneMyApproveLine(Long groupId) {

        List<ApproveLine> lines = approveLineQueryRepository.findByGroupId(groupId);

        // 첫 번째 결재선에서 결재선 설명, 이름, groupId 가져오기
        ApproveLine firstLine = lines.get(0);

        if (lines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE);
        }

        return DocMapper.toMyApproveLineDetailResponseDTO(lines, firstLine);
    }

    // 대기 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<WaitingDocListResponseDTO> getWaitingDocList(Pageable pageable, String empId) {

        // doc id 페이징 처리
        Page<Doc> docs = docQueryRepository.findWaitingDocsByUser(empId, pageable);

        return docs.map(DocMapper::toWaitingDocListResponseDTO);
    }

    // 대기 문서 상세 조회
    @Transactional(readOnly = true)
    public WaitingDocDetailResponseDTO getOneWaitingDoc(Long docId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        return DocMapper.toWaitingDocDetailResponseDTO(doc);
    }

    // 처리 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<ProcessedDocListResponseDTO> getProcessedDocList(Pageable pageable, String empId) {

        Page<Doc> docs = docQueryRepository.findProcessedDocsByUser(empId, pageable);

        return docs.map(DocMapper::toProcessedDocListResponseDTO);
    }

    // 처리 문서 상세 조회
    @Transactional(readOnly = true)
    public ProcessedDocDetailResponseDTO getOneProcessedDoc(Long docId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        return DocMapper.toProcessedDocDetailResponseDTO(doc);
    }
}
