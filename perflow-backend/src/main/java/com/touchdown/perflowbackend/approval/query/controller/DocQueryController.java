package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.approval.query.service.DocQueryService;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.hr.query.repository.PositionQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class DocQueryController {

    private final DocQueryService docQueryService;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final PositionQueryRepository positionQueryRepository;

    // 나의 결재선 목록 조회
    @GetMapping("/my-approve-lines")
    public ResponseEntity<Page<MyApproveLineGroupResponseDTO>> getMyApproveLines(Pageable pageable) {

        // todo: 회원 기능 생기면 넣기
        String createUserId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getMyApproveLineList(pageable, createUserId));
    }

    // 나의 결재선 상세 조회
    @GetMapping("/my-approve-lines/{groupId}")
    public ResponseEntity<MyApproveLineDetailResponseDTO> getMyApproveLine(@PathVariable Long groupId) {

        return ResponseEntity.ok(docQueryService.getOneMyApproveLine(groupId));
    }

    // 수신함 문서 목록 조회
    @GetMapping("/inbox")
    public ResponseEntity<Page<InboxDocListResponseDTO>> getInboxDocs(Pageable pageable) {

        // 로그인 한 사원
        String empId = EmployeeUtil.getEmpId();

        // 사원의 부서, 포지션 레벨
        Long deptId = employeeQueryRepository.findDeptIdByEmpId(empId);
        Integer positionLevel = positionQueryRepository.findPositionLevelByEmpId(empId);

        return ResponseEntity.ok(docQueryService.getInboxDocList(pageable, empId, deptId, positionLevel));
    }

    // 수신함 문서 상세 조회
    @GetMapping("inbox/{docId}")
    public ResponseEntity<InboxDocDetailResponseDTO> getInboxDoc(@PathVariable Long docId) {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getOneInboxDoc(docId, empId));
    }

    // 발신함 문서 목록 조회
    @GetMapping("outbox")
    public ResponseEntity<Page<OutboxDocListResponseDTO>> getOutBoxDocs(Pageable pageable) {

        // 로그인 한 사원
        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getOutBoxDocList(pageable, empId));
    }

    // 발신함 문서 상세 조회
    @GetMapping("outbox/{docId}")
    public ResponseEntity<OutboxDocDetailResponseDTO> getOutBoxDoc(@PathVariable Long docId) {

        // 로그인 한 사원
        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getOneOutboxDoc(docId, empId));

    }

    // 대기 문서 목록 조회
    @GetMapping("/waiting-docs")
    public ResponseEntity<Page<WaitingDocListResponseDTO>> getWaitingDocs(Pageable pageable) {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getWaitingDocList(pageable, empId));
    }

    // 대기 문서 목록 검색
    @GetMapping("/waiting-docs/search")
    public ResponseEntity<Page<WaitingDocListResponseDTO>> searchWaitingDocs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String createUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable
    ) {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.searchWaitingDocList(title, createUser, fromDate, toDate, pageable, empId));
    }

    // 대기 문서 상세 조회
    @GetMapping("/waiting-docs/{docId}")
    public ResponseEntity<WaitingDocDetailResponseDTO> getWaitingDoc(@PathVariable Long docId) {

        return ResponseEntity.ok(docQueryService.getOneWaitingDoc(docId));
    }

    // 처리 문서 목록 조회
    @GetMapping("/processed-docs")
    public ResponseEntity<Page<ProcessedDocListResponseDTO>> getProcessedDocs(Pageable pageable) {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getProcessedDocList(pageable, empId));
    }

    // 처리 문서 상세 조회
    @GetMapping("/processed-docs/{docId}")
    public ResponseEntity<ProcessedDocDetailResponseDTO> getProcessedDoc(@PathVariable Long docId) {

        return ResponseEntity.ok(docQueryService.getOneProcessedDoc(docId));
    }
}
