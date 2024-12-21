package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.approval.query.service.DocQueryService;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import com.touchdown.perflowbackend.hr.query.repository.DepartmentQueryRepository;
import com.touchdown.perflowbackend.hr.query.repository.PositionQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 대기 문서 목록 조회
    @GetMapping("/waiting-docs")
    public ResponseEntity<Page<WaitingDocListResponseDTO>> getWaitingDocs(Pageable pageable) {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(docQueryService.getWaitingDocList(pageable, empId));
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
