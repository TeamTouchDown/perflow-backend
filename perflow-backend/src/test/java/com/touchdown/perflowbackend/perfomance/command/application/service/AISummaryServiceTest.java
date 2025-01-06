package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.perfomance.command.application.dto.GeminiCustomResponseDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.QuestionType;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.AiPerfoSummaryCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AISummaryServiceTest {

    @InjectMocks
    private AISummaryService aiSummaryService;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @Mock
    private PerfoQuestionCommandRepository perfoQuestionCommandRepository;

    @Mock
    private PerfoCommandRepository perfoCommandRepository;

    @Mock
    private AiPerfoSummaryCommandRepository aiPerfoSummaryCommandRepository;

    @Mock
    private RestTemplate restTemplate;

    private Employee mockEmployee;
    private Department mockDepartment;
    private Job mockJob;
    private Position mockPosition;
    private Perfoquestion mockQuestion;
    private Perfo mockPerfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Department
        mockDepartment = Department.builder()
                .createDTO(new DepartmentCreateDTO(
                        1L,
                        "인사부",
                        "인사 관리 및 채용",
                        "010-1234-5678",
                        null,
                        null
                ))
                .manageDept(null)
                .pic(null)
                .build();

        // Mock Job
        mockJob = Job.builder()
                .createDTO(new JobCreateDTO(
                        1L,
                        "채용 담당자",
                        "신입 및 경력 사원 채용 관리",
                        Status.ACTIVE
                ))
                .dept(mockDepartment)
                .build();

        // Mock Position
        mockPosition = Position.builder()
                .positionCreateDTO(new PositionCreateDTO(
                        "인턴",
                        1
                ))
                .build();

        // Mock Employee
        mockEmployee = Employee.builder()
                .registerDTO(new EmployeeCreateDTO(
                        "EMP123",
                        1L,
                        1L,
                        1L,
                        "alex",
                        "female",
                        "123423-2342133",
                        500L,
                        "123 Main St",
                        "123423-2342133",
                        "2142343@example.com",
                        LocalDate.of(2024, 12, 31)
                ))
                .position(mockPosition)
                .job(mockJob)
                .department(mockDepartment)
                .build();

        // Mock PerfoQuestion
        mockQuestion = Perfoquestion.builder()
                .dept(mockDepartment)
                .emp(mockEmployee)
                .questionType(QuestionType.SUBJECTIVE)
                .questionContent("What are your strengths?")
                .perfoType(PerfoType.COL)
                .build();

        // Mock Perfo
        mockPerfo = Perfo.builder()
                .perfoQuestion(mockQuestion)
                .perfoEmp(mockEmployee)
                .perfoedEmp(mockEmployee)
                .answer("Hardworking and dedicated.")
                .build();
    }

    @Test
    void testCreateAISummary_EmployeeNotFound() {
        when(employeeCommandRepository.findById("INVALID")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> aiSummaryService.createAISummary("INVALID"));
        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
    }

    @Test
    void testCreateAISummary_PerfoQuestionsNotFound() {
        when(employeeCommandRepository.findById("EMP123")).thenReturn(Optional.of(mockEmployee));
        when(perfoQuestionCommandRepository.findByDept_departmentId(1L)).thenReturn(List.of());

        CustomException exception = assertThrows(CustomException.class, () -> aiSummaryService.createAISummary("EMP123"));
        assertEquals(ErrorCode.NOT_FOUND_PERFOQUESTION, exception.getErrorCode());
    }

    @Test
    void testGetAIResponse_Failure() {
        String input = "Sample input for AI";

        // Mock AI Response as null
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiCustomResponseDTO.class))).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> aiSummaryService.getAIResponse(input));
        assertEquals(ErrorCode.NOT_FOUND_AI_RESPONSE, exception.getErrorCode());
    }

    private GeminiCustomResponseDTO createMockResponse(String summaryText) {
        GeminiCustomResponseDTO response = new GeminiCustomResponseDTO();
        GeminiCustomResponseDTO.Candidate candidate = new GeminiCustomResponseDTO.Candidate();
        GeminiCustomResponseDTO.Content content = new GeminiCustomResponseDTO.Content();
        GeminiCustomResponseDTO.Part part = new GeminiCustomResponseDTO.Part();

        part.setText(summaryText);
        content.setParts(List.of(part));
        candidate.setContent(content);
        response.setCandidates(List.of(candidate));

        return response;
    }
}
