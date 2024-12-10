package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineListResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import com.touchdown.perflowbackend.approval.query.service.DocQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class DocQueryController {

    private final DocQueryService docQueryService;

    @GetMapping("/my-approve-lines")
    public ResponseEntity<Page<MyApproveLineListResponseDTO>> readMyApproveLines(Pageable pageable) {

        // todo: 회원 기능 생기면 넣기
        String createUserId = "23-MK004";

        return ResponseEntity.ok(docQueryService.getApproveLineList(pageable, createUserId));
    }

}
