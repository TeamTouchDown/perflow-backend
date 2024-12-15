package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.service.DocQueryService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 나의 결재선 목록 조회
    @GetMapping("/my-approve-lines")
    public ResponseEntity<Page<MyApproveLineGroupResponseDTO>> getMyApproveLines(Pageable pageable) {

        // todo: 회원 기능 생기면 넣기
        String createUserId = "23-MK004";

        return ResponseEntity.ok(docQueryService.getMyApproveLineList(pageable, createUserId));
    }

    // 나의 결재선 상세 조회
    @GetMapping("/my-approve-lines/{groupId}")
    public ResponseEntity<MyApproveLineDetailResponseDTO> getMyApproveLine(@PathVariable Long groupId) {

        return ResponseEntity.ok(docQueryService.getOneMyApproveLine(groupId));
    }

    // 대기 문서 목록 조회
    @GetMapping("/waiting-docs")
    public ResponseEntity<Page<WaitingDocListResponseDTO>> getWaitingDocs(Pageable pageable) {

//        String empId = EmployeeUtil.getEmpId();
//        String empId = "23-IT003";
        String empId = "23-OP005";

        return ResponseEntity.ok(docQueryService.getWaitingDocList(pageable, empId));
    }

    // 대기 문서 상세 조회
    @GetMapping("/waiting-docs/{docId}")
    public ResponseEntity<WaitingDocDetailResponseDTO> getWaitingDoc(@PathVariable Long docId) {

        return ResponseEntity.ok(docQueryService.getOneWaitingDoc(docId));
    }
}
