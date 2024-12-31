package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.approval.query.repository.ApproveSbjQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.DepartmentMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class) // Mockito 설정
class DocQueryServiceTest {

    @InjectMocks
    private DocQueryService docQueryService;    // 테스트 대상 서비스

    @Mock
    private DocQueryRepository docQueryRepository;

    @Mock
    private ApproveSbjQueryRepository approveSbjQueryRepository;

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
    @DisplayName("대기 문서 목록 검색 테스트")
    void testSearchWaitingDocList() {

        // Mock
        String title = "테스트";
        String createUser = "이재무";
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 30);
        String empId = "23-FN002";
        Pageable pageable = PageRequest.of(0, 10);

        Department department = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Job job = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department);
        Position position = createMockPosition("대리", 3);
        Employee employee = createMockEmployee(empId, createUser, department, job, position);
        Template template = createMockTemplate(4L, "지출결의서");
        Doc doc = createMockDoc(title, employee, template);

        Page<Doc> mockPage = new PageImpl<>(List.of(doc));

        // Stub
        Mockito.when(docQueryRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(mockPage);

        Page<WaitingDocListResponseDTO> result =  docQueryService.searchWaitingDocList(title, createUser, fromDate, toDate, pageable, empId);

        assertNotNull(result);
        assertEquals("테스트", result.getContent().get(0).getTitle());
        assertEquals("이재무", result.getContent().get(0).getCreateUserName());

        // Verify
        Mockito.verify(docQueryRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    @DisplayName("문서 상세 조회 테스트")
    void testGetOneWaitingDoc() {

        // Mock
        Long docId = 1L;
        Department department = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Job job = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department);
        Position position = createMockPosition("대리", 3);
        Employee employee = createMockEmployee("23-FN002", "이재무", department, job, position);
        Template template = createMockTemplate(4L, "지출결의서");
        Doc doc = createMockDoc("2024_12_20 테스트1", employee, template);
        ApproveLine approveLine = createMockApproveLine(doc, employee);
        ApproveSbj approveSbj = createMockApproveSbj(approveLine, employee);
        DocShareObj docShareObj = createMockDocShareObj(doc, employee);

        // auto-increment pk 설정
        ReflectionTestUtils.setField(approveLine, "approveLineId", 1L);
        ReflectionTestUtils.setField(approveSbj, "approveSbjId", 100L);

        approveSbj.updateStatus(Status.ACTIVATED);
        approveSbj.updateComment(null);
        approveLine.addApproveSbj(approveSbj);
        doc.getApproveLines().add(approveLine);
        doc.getShares().add(docShareObj);

        Map<String, String> mockFields = Map.of(
                "VENDOR1", "a마트",
                "USAGE1", "야유회 준비",
                "AMOUNT1", "72000"
        );

        mockFields.forEach((key, value) -> {
            DocField docField = createMockDocField(doc, key, value);
            doc.getDocFields().add(docField);
        });

        WaitingDocDetailResponseDTO responseDTO = WaitingDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .createUserName(employee.getName())
                .createUserDept(department.getName())
                .createUserPosition(position.getName())
                .createDatetime(doc.getCreateDatetime())
                .fields(Collections.emptyMap())
                .approveLines(List.of(createMockApproveLineDTO(approveLine, approveSbj)))
                .shares(List.of(createMockShareDTO(docShareObj, employee)))
                .build();

        // Stub
        doReturn(Optional.of(doc)).when(docQueryRepository).findById(docId);

        WaitingDocDetailResponseDTO result = docQueryService.getOneWaitingDoc(docId);

        assertNotNull(result);
        assertEquals("2024_12_20 테스트1", result.getTitle());
        assertEquals("이재무", result.getCreateUserName());
        assertEquals("재무부", result.getCreateUserDept());
        assertEquals("대리", result.getCreateUserPosition());

        assertEquals(mockFields.size(), result.getFields().size());
        mockFields.forEach((key, value) -> assertEquals(value, result.getFields().get(key)));

        assertEquals(1, result.getApproveLines().size());
        WaitingDocApproveLineDTO lineDTO = result.getApproveLines().get(0);
        assertEquals(approveLine.getApproveLineId(), lineDTO.getApproveLineId());

        WaitingDocShareDTO shareDTO = result.getShares().get(0);
        assertEquals(docShareObj.getShareEmpDeptType(), shareDTO.getShareEmpDeptType());
        assertEquals(employee.getEmpId(), shareDTO.getEmpIds().get(0));
        assertEquals(employee.getName(), shareDTO.getEmpNames().get(0));


        Mockito.verify(docQueryRepository, Mockito.times(1)).findById(docId);
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
    @DisplayName("처리 문서 목록 검색 테스트")
    void testSearchProcessedDocList() {

        // Mock
        String title = "테스트";
        String createUser = "이재무";
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 30);
        String empId = "23-FN002";
        Pageable pageable = PageRequest.of(0, 10);

        Department department = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Job job = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department);
        Position position = createMockPosition("대리", 3);
        Employee employee = createMockEmployee(empId, createUser, department, job, position);
        Template template = createMockTemplate(4L, "지출결의서");
        Doc doc = createMockDoc(title, employee, template);
        ApproveLine approveLine = createMockApproveLine(doc, employee);
        ApproveSbj approveSbj = createMockApproveSbj(approveLine, employee);

        approveSbj.updateStatus(Status.APPROVED);
        approveSbj.updateComment("수고하셨습니다~");
        approveLine.addApproveSbj(approveSbj);

        doc.getApproveLines().add(approveLine);
        approveLine.getApproveSbjs().add(approveSbj);

        Page<ApproveSbj> mockPage = new PageImpl<>(List.of(approveSbj));

        // Stub
        Mockito.when(approveSbjQueryRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(mockPage);

        Page<ProcessedDocListResponseDTO> result = docQueryService.searchProcessedDocList(title, createUser, fromDate, toDate, pageable, empId);

        assertNotNull(result);
        assertEquals("테스트", result.getContent().get(0).getTitle());
        assertEquals("이재무", result.getContent().get(0).getCreateUserName());
        assertEquals(Status.APPROVED, result.getContent().get(0).getApproveSbjStatus());

        Mockito.verify(approveSbjQueryRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    @DisplayName("수신함 문서 목록 조회 테스트")
    void testGetInboxDocList() {

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
    @DisplayName("수신함 문서 목록 검색 테스트")
    void testSearchInboxDocList() {

        // Mock
        String title = "테스트";
        String createUser = "이재무";
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        String empId = "23-FN002";
        Long deptId = 2L;
        Integer positionLevel = 3;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDatetime").descending()); // 정렬 포함

        Department department = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Job job = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department);
        Position position = createMockPosition("대리", 3);
        Employee employee = createMockEmployee(empId, createUser, department, job, position);
        Template template = createMockTemplate(4L, "지출결의서");
        Doc doc = createMockDoc(title, employee, template);
        ApproveLine approveLine = createMockApproveLine(doc, employee);
        ApproveSbj approveSbj = createMockApproveSbj(approveLine, employee);

        approveSbj.updateStatus(Status.APPROVED);
        approveSbj.updateComment("수고하셨습니다~");
        approveLine.addApproveSbj(approveSbj);

        InboxDocListResponseDTO responseDTO = InboxDocListResponseDTO.builder()
                .docId(doc.getDocId())
                .templateId(template.getTemplateId())
                .title(doc.getTitle())
                .createUserName(employee.getName())
                .createDatetime(doc.getCreateDatetime())
                .processDatetime(doc.getUpdateDatetime())
                .status("진행")
                .approveLineId(approveLine.getApproveLineId())
                .approveSbjId(approveSbj.getApproveSbjId())
                .build();

        Page<Doc> mockPage = new PageImpl<>(List.of(doc));

        // Stub
        doReturn(mockPage).when(docQueryRepository).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));

        Page<InboxDocListResponseDTO> result = docQueryService.searchInboxDocList(
                title, createUser, fromDate, toDate, pageable, empId, deptId, positionLevel
        );

        // verify
        assertNotNull(result);
        assertEquals("테스트", result.getContent().get(0).getTitle());
        assertEquals("이재무", result.getContent().get(0).getCreateUserName());
        assertEquals("진행", result.getContent().get(0).getStatus());

        Mockito.verify(docQueryRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    @DisplayName("발신함 문서 목록 조회 테스트")
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

    @Test
    @DisplayName("발신함 목록 검색 조회 테스트")
    void testSearchOutboxDocList() {

        // Mock
        String title = "테스트";
        String createUser = "이재무";
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        String empId = "23-FN002";
        Long deptId = 2L;
        Integer positionLevel = 3;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDatetime").descending()); // 정렬 포함

        Department department = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Job job = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department);
        Position position = createMockPosition("대리", 3);
        Employee employee = createMockEmployee(empId, createUser, department, job, position);
        Template template = createMockTemplate(4L, "지출결의서");
        Doc doc = createMockDoc(title, employee, template);

        doc.updateStatus(Status.ACTIVATED);

        OutboxDocListResponseDTO responseDTO = OutboxDocListResponseDTO.builder()
                .docId(doc.getDocId())
                .templateId(template.getTemplateId())
                .title(doc.getTitle())
                .createDatetime(doc.getCreateDatetime())
                .status(doc.getStatus())
                .build();

        Page<Doc> mockPage = new PageImpl<>(List.of(doc));

        // Stub
        doReturn(mockPage).when(docQueryRepository).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));

        Page<OutboxDocListResponseDTO> result = docQueryService.searchOutboxDocList(
                title, fromDate, toDate, pageable, empId
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("테스트", result.getContent().get(0).getTitle());
        assertEquals(Status.ACTIVATED, result.getContent().get(0).getStatus());
        assertEquals(template.getTemplateId(), result.getContent().get(0).getTemplateId());

        Mockito.verify(docQueryRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.eq(pageable));

    }

    // ---------------------------------------------------------------------------------------------
    // Mock 데이터 생성 메소드

    // Department Mock 생성
    private Department createMockDepartment(String name, String responsibility) {

        DepartmentCreateDTO departmentCreateDTO = new DepartmentCreateDTO();
        departmentCreateDTO.setDepartmentId(2L);
        departmentCreateDTO.setName(name);
        departmentCreateDTO.setResponsibility(responsibility);
        departmentCreateDTO.setContact("010-1234-5678");

        return DepartmentMapper.toEntity(departmentCreateDTO, null);
    }

    // Job Mock 생성
    private Job createMockJob(String name, String responsibility, Department department) {

        return Job.builder()
                .createDTO(new JobCreateDTO(5L, name, responsibility, com.touchdown.perflowbackend.hr.command.domain.aggregate.Status.ACTIVE))
                .dept(department)
                .build();
    }

    // Position Mock 생성
    private Position createMockPosition(String name, int level) {

        return Position.builder()
                .positionCreateDTO(new PositionCreateDTO(name, level))
                .build();
    }

    // Employee Mock 생성
    private Employee createMockEmployee(String empId, String name, Department department, Job job, Position position) {

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .empId(empId)
                .positionId(3L)
                .jobId(5L)
                .departmentId(2L)
                .name(name)
                .gender("Male")
                .rrn("880305-2345678")
                .pay(6000000L)
                .address("부산광역시 해운대구")
                .contact("010-2345-6789")
                .email("finance.lee@example.com")
                .joinDate(LocalDate.of(2023, 2, 10))
                .build();

        return Employee.builder()
                .registerDTO(employeeCreateDTO)
                .position(position)
                .job(job)
                .department(department)
                .build();
    }

    // Doc Mock 생성
    private Doc createMockDoc(String title, Employee createUser, Template template) {

        return Doc.builder()
                .title(title)
                .template(template)
                .createUser(createUser)
                .build();
    }

    // ApproveLine Mock 생성
    private ApproveLine createMockApproveLine(Doc doc, Employee createUser) {

        return ApproveLine.builder()
                .doc(doc)
                .approveTemplateType(ApproveTemplateType.MANUAL)
                .name("결재선 1")
                .approveType(ApproveType.SEQ)
                .approveLineOrder(1L)
                .createUser(createUser)
                .build();
    }

    // ApproveSbj Mock 생성
    private ApproveSbj createMockApproveSbj(ApproveLine approveLine, Employee sbjUser) {

        return ApproveSbj.builder()
                .approveLine(approveLine)
                .sbjUser(sbjUser)
                .empDeptType(EmpDeptType.EMPLOYEE)
                .dept(sbjUser.getDept())
                .isPll(false)
                .build();
    }

    // Template Mock 생성
    private Template createMockTemplate(Long templateId, String name) {

        return Template.builder()
                .templateId(templateId)
                .name(name)
                .build();
    }

    // DocShareObj Mock 생성
    private DocShareObj createMockDocShareObj(Doc doc, Employee employee) {

        return DocShareObj.builder()
                .doc(doc)
                .shareAddUser(employee)
                .shareEmpDeptType(EmpDeptType.EMPLOYEE)
                .shareObjUser(employee)
                .build();
    }

    private DocField createMockDocField(Doc doc, String fieldKey, String userValue) {

        return DocField.builder()
                .doc(doc)
                .fieldKey(fieldKey)
                .userValue(userValue)
                .build();
    }

    // ---------------------------------------------------------------------------------------------
    // DTO 생성 메소드

    private WaitingDocApproveLineDTO createMockApproveLineDTO(ApproveLine approveLine, ApproveSbj approveSbj) {

        System.out.println("ApproveLine ID: " + approveLine.getApproveLineId());

        return WaitingDocApproveLineDTO.builder()
                .approveLineId(approveLine.getApproveLineId())
                .groupId(approveLine.getGroupId())
                .approveType(approveLine.getApproveType())
                .approveLineOrder(approveLine.getApproveLineOrder())
                .approveSbjs(List.of(
                        WaitingDocApproveSbjDTO.builder()
                                .approveSbjId(approveSbj.getApproveSbjId())
                                .empDeptType(approveSbj.getEmpDeptType())
                                .empId(approveSbj.getSbjUser().getEmpId())
                                .empName(approveSbj.getSbjUser().getName())
                                .status(approveSbj.getStatus())
                                .comment(approveSbj.getComment())
                                .build()
                ))
                .build();
    }

    // ShareDTO 생성 메소드
    private WaitingDocShareDTO createMockShareDTO(DocShareObj shareObj, Employee employee) {
        return WaitingDocShareDTO.builder()
                .shareEmpDeptType(shareObj.getShareEmpDeptType())
                .empIds(List.of(employee.getEmpId()))
                .empNames(List.of(employee.getName()))
                .build();
    }

}