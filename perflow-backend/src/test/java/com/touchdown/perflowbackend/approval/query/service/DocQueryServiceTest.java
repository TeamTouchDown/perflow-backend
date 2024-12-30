package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.query.dto.InboxDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.OutboxDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.ProcessedDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class) // Mockito 설정
class DocQueryServiceTest {

    @InjectMocks
    private DocQueryService docQueryService;    // 테스트 대상 서비스

    @Mock
    private DocQueryRepository docQueryRepository;

    @Test
    @DisplayName("대기 문서 목록 조회 테스트")
    void testGetWaitingDocList() {

        // Mock
        String empId = "23-FN002";
        Pageable pageable = PageRequest.of(0, 10);

        WaitingDocListResponseDTO mockDTO = WaitingDocListResponseDTO.builder()
                .docId(1L)
                .templateId(4L)
                .title("2024_12_30 테스트 문서1")
                .createUserName("이재무")
                .empId(empId)
                .approveLineId(10L)
                .approveSbjId(5L)
                .createDatetime(LocalDateTime.now())
                .build();
        Page<WaitingDocListResponseDTO> mockPage = new PageImpl<>(List.of(mockDTO), pageable, 1);

        // Stub
        Mockito.when(docQueryRepository.findWaitingDocsByUser(empId, pageable)).thenReturn(mockPage);
        Page<WaitingDocListResponseDTO> result = docQueryService.getWaitingDocList(pageable, empId);

        assertNotNull(result);
        assertEquals("2024_12_30 테스트 문서1", result.getContent().get(0).getTitle());

        // verify
        Mockito.verify(docQueryRepository, Mockito.times(1)).findWaitingDocsByUser(empId, pageable);
    }

    @Test
    @DisplayName("처리 문서 목록 조회 테스트")
    void testGetProcessedDocList() {

        // Mock
        String empId = "23-HR005";
        Pageable pageable = PageRequest.of(0, 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createDatetime = LocalDateTime.parse("2024-12-28 18:56:01", formatter);
        LocalDateTime processDatetime = LocalDateTime.parse("2024-12-29 12:49:04", formatter);

        ProcessedDocListResponseDTO mockDTO = ProcessedDocListResponseDTO.builder()
                .docId(1L)
                .templateId(4L)
                .title("2024_12_30 테스트 문서2")
                .createUserName("이재무")
                .empId(empId)
                .approveLineId(10L)
                .approveSbjId(5L)
                .createDatetime(createDatetime)
                .approveSbjStatus(Status.APPROVED)
                .processDatetime(processDatetime)
                .comment("수고하셨습니다^^")
                .build();

        Page<ProcessedDocListResponseDTO> mockPage = new PageImpl<>(List.of(mockDTO), pageable, 1);

        // Stub
        Mockito.when(docQueryRepository.findProcessedDocs(pageable, empId)).thenReturn(mockPage);
        Page<ProcessedDocListResponseDTO> result = docQueryService.getProcessedDocList(pageable, empId);

        assertNotNull(result);
        assertEquals("2024_12_30 테스트 문서2", result.getContent().get(0).getTitle());

        // verify
        Mockito.verify(docQueryRepository, Mockito.times(1)).findProcessedDocs(pageable, empId);
    }

    @Test
    @DisplayName("수신함 문서 목록 조회 테스트")
    void testSearchInboxDocList() {

        // Mock
        String empId = "23-HR005";
        Long deptId = 3L;
        Integer positionLevel = 2;
        Pageable pageable = PageRequest.of(0, 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createDatetime = LocalDateTime.parse("2024-12-28 18:56:01", formatter);
        LocalDateTime processDatetime = LocalDateTime.parse("2024-12-29 12:49:04", formatter);

        InboxDocListResponseDTO mockDTO = InboxDocListResponseDTO.builder()
                .docId(1L)
                .templateId(4L)
                .title("2024_12_30 테스트 문서3")
                .createUserName("이재무")
                .createDatetime(createDatetime)
                .processDatetime(processDatetime)
                .status("반려")
                .approveLineId(10L)
                .approveSbjId(5L)
                .build();

        Page<InboxDocListResponseDTO> mockPage = new PageImpl<>(List.of(mockDTO), pageable, 1);

        // Stub
        Mockito.when(docQueryRepository.findInboxDocs(pageable, empId, deptId, positionLevel)).thenReturn(mockPage);
        Page<InboxDocListResponseDTO> result = docQueryService.getInboxDocList(pageable, empId, deptId, positionLevel);

        assertNotNull(result);
        assertEquals("2024_12_30 테스트 문서3", result.getContent().get(0).getTitle());

        // verify
        Mockito.verify(docQueryRepository, Mockito.times(1)).findInboxDocs(pageable, empId, deptId, positionLevel);
    }

    @Test
    @DisplayName("발신함 문서 목록 조회")
    void testGetOutboxDocList() {

        String empId = "23-IT101";
        Pageable pageable = PageRequest.of(0, 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createDatetime = LocalDateTime.parse("2024-12-28 18:56:01", formatter);

        OutboxDocListResponseDTO mockDTO = OutboxDocListResponseDTO.builder()
                .docId(101L)
                .templateId(5L)
                .title("2024_12_30 테스트 문서4")
                .createDatetime(createDatetime)
                .status(Status.PENDING)
                .build();

        Page<OutboxDocListResponseDTO> mockPage = new PageImpl<>(List.of(mockDTO), pageable, 1);

        // Stub
        Mockito.when(docQueryRepository.findOutBoxDocs(pageable, empId)).thenReturn(mockPage);
        Page<OutboxDocListResponseDTO> result = docQueryService.getOutboxDocList(pageable, empId);

        assertNotNull(result);
        assertEquals("2024_12_30 테스트 문서4", result.getContent().get(0).getTitle());

        // verify
        Mockito.verify(docQueryRepository, Mockito.times(1)).findOutBoxDocs(pageable, empId);

    }
}