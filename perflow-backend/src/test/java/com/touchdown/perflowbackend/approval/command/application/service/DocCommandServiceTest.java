package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocFieldCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.query.dto.ApproveSbjDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class) // Mockito 설정
class DocCommandServiceTest {

    @InjectMocks
    private DocCommandService docCommandService;    // 테스트 대상 서비스

    @Mock
    private DocCommandRepository docCommandRepository;
    @Mock
    private EmployeeCommandRepository employeeCommandRepository;
    @Mock
    private TemplateCommandRepository templateCommandRepository;
    @Mock
    private DocFieldCommandRepository docFieldCommandRepository;

    @Test
    @DisplayName("결재 문서 생성 테스트")
    void testCreateNewDoc() {

        // Mock
        String createUserId = "23-FN002";
        Long templateId = 4L;

        Department department1 = createMockDepartment("재무부", "재무 관리 및 예산 책정");
        Department department2 = createMockDepartment("운영부", "운영 및 프로세스 관리");
        Department department3 = createMockDepartment("인사부", "인사 관리 및 채용");

        Job job1 = createMockJob("재무부 팀장", "재무부 총괄 및 업무 조율", department1);
        Job job2 = createMockJob("운영부 팀장", "운영부 총괄 및 업무 조율", department2);
        Job job3 = createMockJob("인사부 팀장", "인사부 총괄 및 업무 조율", department3);

        Position position1 = createMockPosition("대리", 3);
        Position position2 = createMockPosition("부장", 5);
        Position position3 = createMockPosition("사원", 2);

        Employee employee = createMockEmployee(createUserId, "이재무", department1, job1, position1);
        Employee approveEmployee = createMockEmployee("23-HR001", "김인사", department2, job2, position2);
        Employee shareEmployee = createMockEmployee("23-OP005", "정운영", department3, job3, position3);

        Template template = createMockTemplate(templateId, "지출결의서");

        Map<String, String> fields = Map.of(
                "VENDOR", "A마트",
                "USAGE", "사무용품 구입",
                "AMOUNT", "10000"
        );

        List<ApproveLineRequestDTO> approveLines = List.of(
                ApproveLineRequestDTO.builder()
                        .approveType(ApproveType.SEQ)
                        .approveLineOrder(1L)
                        .approveSbjs(List.of(
                                ApproveSbjDTO.builder()
                                        .empId("23-HR001")
                                        .empDeptType(EmpDeptType.EMPLOYEE)
                                        .build()
                        ))
                        .build()
        );

        List<ShareDTO> shares = List.of(
                ShareDTO.builder()
                        .shareEmpDeptType(EmpDeptType.EMPLOYEE)
                        .employees(List.of("23-OP005"))
                        .build()
        );

        DocCreateRequestDTO request = DocCreateRequestDTO.builder()
                .templateId(templateId)
                .fields(fields)
                .approveLines(approveLines)
                .shares(shares)
                .build();

        Doc doc = createMockDoc("테스트 문서", employee, template);

        // Stub
        doReturn(Optional.of(employee)).when(employeeCommandRepository).findById(createUserId);
        doReturn(Optional.of(template)).when(templateCommandRepository).findById(templateId);
        doReturn(doc).when(docCommandRepository).save(Mockito.any(Doc.class));
        doReturn(List.of(approveEmployee, shareEmployee)).when(employeeCommandRepository).findAllById(Mockito.anySet());

        docCommandService.createNewDoc(request, createUserId);

        // Verify
        Mockito.verify(employeeCommandRepository, Mockito.times(1)).findById(createUserId);
        Mockito.verify(templateCommandRepository, Mockito.times(1)).findById(templateId);
        Mockito.verify(docCommandRepository, Mockito.times(1)).save(Mockito.any(Doc.class));
        Mockito.verify(docFieldCommandRepository, Mockito.times(fields.size())).save(Mockito.any(DocField.class));
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
}