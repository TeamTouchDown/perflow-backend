package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.*;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.*;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoHistoryCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvalutionCommandServiceTest {

    @InjectMocks
    private EvalutionCommandService evalutionCommandService;

    @Mock
    private PerfoCommandRepository perfoCommandRepository;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @Mock
    private PerfoQuestionCommandRepository perfoQuestionCommandRepository;

    @Mock
    private DepartmentCommandRepository departmentCommandRepository;

    @Mock
    private HrPerfoCommandRepository hrPerfoCommandRepository;

    @Mock
    private HrPerfoHistoryCommandRepository hrPerfoHistoryCommandRepository;

    private Employee mockPerfoEmp;
    private Employee mockPerfoedEmp;
    private Department mockDepartment;
    private Perfoquestion mockPerfoQuestion;
    private Perfo mockPerfo;
    private Job mockJob;
    private Position mockPosition;

    @BeforeEach
    void setUp() {
        // Mock Department
        DepartmentCreateDTO deptDTO = new DepartmentCreateDTO(
                1L,
                "인사부",
                "인사 관리 및 채용",
                "010-1234-5678",
                null,
                null
        );

        mockDepartment = Department.builder()
                .createDTO(deptDTO)
                .manageDept(null)
                .pic(null)
                .build();

        // Mock Job
        JobCreateDTO jobDTO = new JobCreateDTO(
                1L,
                "채용 담당자",
                "신입 및 경력 사원 채용 관리",
                Status.ACTIVE
        );

        mockJob = Job.builder()
                .createDTO(jobDTO)
                .dept(mockDepartment)
                .build();

        // Mock Position
        PositionCreateDTO posDTO = new PositionCreateDTO(
                "인턴",
                1
        );

        mockPosition = Position.builder()
                .positionCreateDTO(posDTO)
                .build();

        // Mock Employees
        EmployeeCreateDTO empCreateDTO1 = new EmployeeCreateDTO(
                "EMP001",
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
        );

        // 직접 empId를 설정
        mockPerfoEmp = Employee.builder()
                .registerDTO(empCreateDTO1)
                .position(mockPosition)
                .job(mockJob)
                .department(mockDepartment)
                .build();

        EmployeeCreateDTO empCreateDTO2 = new EmployeeCreateDTO(
                "EMP002",
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
        );

        // 직접 empId를 설정
        mockPerfoedEmp = Employee.builder()
                .registerDTO(empCreateDTO2)
                .position(mockPosition)
                .job(mockJob)
                .department(mockDepartment)
                .build();

        // Mock PerfoQuestion
        mockPerfoQuestion = Perfoquestion.builder()
                .dept(mockDepartment)
                .emp(mockPerfoEmp)
                .questionType(QuestionType.SUBJECTIVE)
                .questionContent("What are your strengths?")
                .perfoType(PerfoType.COL)
                .build();

        // Mock Perfo
        mockPerfo = Perfo.builder()
                .perfoQuestion(mockPerfoQuestion)
                .perfoEmp(mockPerfoEmp)
                .perfoedEmp(mockPerfoedEmp)
                .answer("Hardworking and dedicated.")
                .build();
    }

    // Helper methods
    private EvalutionListDTO createEvalutionListDTO() {
        EvalutionDetailDTO detail1 = EvalutionDetailDTO.builder()
                .questionId(100L) // perfoQuestionId와 일치
                .answer("Excellent communication.")
                .build();

        return EvalutionListDTO.builder()
                .perfoedEmpId("EMP002")
                .answers(Arrays.asList(detail1))
                .build();
    }

    private CreateQuestionRequestDTO createQuestionRequestDTO() {
        return CreateQuestionRequestDTO.builder()
                .questionContent("How do you handle stress?")
                .questionType("SUBJECTIVE")
                .build();
    }

    private UpdateQuestionRequestDTO updateQuestionRequestDTO() {
        return UpdateQuestionRequestDTO.builder()
                .questionContext("Describe your problem-solving skills.") // 'questionContext'에서 'questionContent'로 수정
                .questionType("SUBJECTIVE")
                .build();
    }

    private CreatePerfoAdjustmentDTO createPerfoAdjustmentDTO() {
        return CreatePerfoAdjustmentDTO.builder()
                .degree(1L)
                .colScore(25L)
                .downScore(22L)
                .build();
    }

    // Test Cases

    // createPerfo Tests
    @Test
    void testCreatePerfo_Success() {
        // Arrange
        EvalutionListDTO evalutionListDTO = createEvalutionListDTO();

        // Using Mockito's mockStatic feature (requires mockito-inline dependency)
        try (MockedStatic<PerformanceMapper> mockedMapper = mockStatic(PerformanceMapper.class)) {
            // Use argument matchers instead of exact instances
            mockedMapper.when(() -> PerformanceMapper.evaluationAnswertoPerfo(
                            any(EvalutionListDTO.class),
                            any(Employee.class),
                            any(Employee.class),
                            any(PerfoQuestionCommandRepository.class)))
                    .thenReturn(Arrays.asList(mockPerfo));

            when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockPerfoEmp));
            when(employeeCommandRepository.findById("EMP002")).thenReturn(Optional.of(mockPerfoedEmp));

            when(perfoCommandRepository.saveAll(anyList())).thenReturn(Arrays.asList(mockPerfo));

            // Act & Assert
            assertDoesNotThrow(() -> evalutionCommandService.createPerfo(evalutionListDTO, "EMP001"));

            // Verify interactions
            verify(employeeCommandRepository, times(2)).findById(anyString());
            verify(perfoQuestionCommandRepository, times(0)).findById(anyLong()); // Because mapper is mocked
            verify(perfoCommandRepository, times(1)).saveAll(anyList());

            // Verify that the mapper was called correctly
            mockedMapper.verify(() -> PerformanceMapper.evaluationAnswertoPerfo(
                    evalutionListDTO,
                    mockPerfoEmp,
                    mockPerfoedEmp,
                    perfoQuestionCommandRepository), times(1));
        }
    }

    @Test
    void testCreatePerfo_EmpNotFound() {
        // Arrange
        EvalutionListDTO evalutionListDTO = createEvalutionListDTO();

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.createPerfo(evalutionListDTO, "EMP001")
        );

        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(employeeCommandRepository, times(0)).findById("EMP002");
        verify(perfoQuestionCommandRepository, times(0)).findById(anyLong());
        verify(perfoCommandRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testCreatePerfo_DepartmentMismatch() {
        // Arrange
        EvalutionListDTO evalutionListDTO = createEvalutionListDTO();

        // 다른 부서로 설정
        Department otherDept = Department.builder()
                .createDTO(new DepartmentCreateDTO(
                        2L,
                        "HR",
                        "Human Resources",
                        "010-9876-5432",
                        null,
                        null
                ))
                .manageDept(null)
                .pic(null)
                .build();

        // 새로운 Employee 인스턴스 생성 (다른 부서)
        Employee otherPerfoedEmp = Employee.builder()
                .registerDTO(new EmployeeCreateDTO(
                        "EMP002",
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
                .department(otherDept)
                .build();

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockPerfoEmp));
        when(employeeCommandRepository.findById("EMP002")).thenReturn(Optional.of(otherPerfoedEmp));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.createPerfo(evalutionListDTO, "EMP001")
        );

        assertEquals(ErrorCode.NOT_MATCH_DEPARTMENT, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(2)).findById(anyString());
        verify(perfoQuestionCommandRepository, times(0)).findById(anyLong());
        verify(perfoCommandRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testUpdatePerfo_EmpNotFound() {
        // Arrange
        EvalutionListDTO evalutionListDTO = createEvalutionListDTO();

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.updatePerfo(evalutionListDTO, "EMP001")
        );

        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(perfoCommandRepository, times(0)).findByPerfoEmp_EmpIdAndPerfoedEmp_EmpId(anyString(), anyString());
        verify(perfoCommandRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testUpdatePerfo_DepartmentMismatch() {
        // Arrange
        EvalutionListDTO evalutionListDTO = createEvalutionListDTO();

        // 다른 부서로 설정
        Department otherDept = Department.builder()
                .createDTO(new DepartmentCreateDTO(
                        2L,
                        "HR",
                        "Human Resources",
                        "010-9876-5432",
                        null,
                        null
                ))
                .manageDept(null)
                .pic(null)
                .build();

        // 새로운 Employee 인스턴스 생성 (다른 부서)
        Employee otherPerfoedEmp = Employee.builder()
                .registerDTO(new EmployeeCreateDTO(
                        "EMP002",
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
                .department(otherDept)
                .build();

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockPerfoEmp));
        when(employeeCommandRepository.findById("EMP002")).thenReturn(Optional.of(otherPerfoedEmp));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.updatePerfo(evalutionListDTO, "EMP001")
        );

        assertEquals(ErrorCode.NOT_MATCH_DEPARTMENT, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(2)).findById(anyString());
        verify(perfoCommandRepository, times(0)).findByPerfoEmp_EmpIdAndPerfoedEmp_EmpId(anyString(), anyString());
        verify(perfoCommandRepository, times(0)).saveAll(anyList());
    }

    // createQuestion Tests

    @Test
    void testCreateQuestion_EmpNotFound() {
        // Arrange
        CreateQuestionRequestDTO createQuestionRequestDTO = createQuestionRequestDTO();

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.createQuestion("EMP001", createQuestionRequestDTO)
        );

        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(departmentCommandRepository, times(0)).findById(anyLong());
        verify(perfoQuestionCommandRepository, times(0)).save(any(Perfoquestion.class));
    }

    // updateQuestion Tests
    @Test
    void testUpdateQuestion_Success() {
        // Arrange
        UpdateQuestionRequestDTO updateQuestionRequestDTO = updateQuestionRequestDTO();
        Long perfoQuestionId = 100L;

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockPerfoEmp));
        when(perfoQuestionCommandRepository.findByperfoQuestionId(perfoQuestionId))
                .thenReturn(mockPerfoQuestion);

        // Act & Assert
        assertDoesNotThrow(() ->
                evalutionCommandService.updateQuestion("EMP001", perfoQuestionId, updateQuestionRequestDTO)
        );

        // Verify interactions
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(perfoQuestionCommandRepository, times(1)).findByperfoQuestionId(perfoQuestionId);
        verify(perfoQuestionCommandRepository, times(1)).save(mockPerfoQuestion);
    }

    @Test
    void testUpdateQuestion_EmpNotFound() {
        // Arrange
        UpdateQuestionRequestDTO updateQuestionRequestDTO = updateQuestionRequestDTO();
        Long perfoQuestionId = 100L;

        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () ->
                evalutionCommandService.updateQuestion("EMP001", perfoQuestionId, updateQuestionRequestDTO)
        );

        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());

        // Verify interactions
        verify(employeeCommandRepository, times(1)).findById("EMP001");
        verify(perfoQuestionCommandRepository, times(0)).findByperfoQuestionId(anyLong());
        verify(perfoQuestionCommandRepository, times(0)).save(any(Perfoquestion.class));
    }



}
