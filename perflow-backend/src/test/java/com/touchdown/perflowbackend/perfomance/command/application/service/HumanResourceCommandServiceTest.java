//package com.touchdown.perflowbackend.perfomance.command.application.service;
//
//import com.touchdown.perflowbackend.common.exception.CustomException;
//import com.touchdown.perflowbackend.common.exception.ErrorCode;
//import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
//import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
//import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
//import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
//import com.touchdown.perflowbackend.hr.command.application.dto.job.JobCreateDTO;
//import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
//import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
//import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
//import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
//import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
//import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateInquiryRequestDTO;
//import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.*;
//import com.touchdown.perflowbackend.perfomance.command.domain.repository.*;
//import com.touchdown.perflowbackend.perfomance.command.infrastructure.repository.HrPerfoHistoryRepository;
//import com.touchdown.perflowbackend.perfomance.command.infrastructure.repository.HrPerfoInquiryRepository;
//import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.Year;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class HumanResourceCommandServiceTest {
//
//    @InjectMocks
//    private HumanResourceCommandService humanResourceCommandService;
//
//    @Mock
//    private KpiCommandRepository kpiCommandRepository;
//
//    @Mock
//    private HrPerfoHistoryRepository hrPerfoHistoryRepository;
//
//    @Mock
//    private EmployeeCommandRepository employeeCommandRepository;
//
//    @Mock
//    private WeightCommandRepository weightCommandRepository;
//
//    @Mock
//    private HrPerfoCommandRepository hrPerfoCommandRepository;
//
//    @Mock
//    private HrPerfoInquiryRepository hrPerfoInquiryRepository;
//
//    private Employee mockEmp;
//    private Employee mockPerfoedEmp;
//    private Weight mockWeight;
//    private HrPerfo mockHrPerfo;
//    private HrPerfoInquiry mockHrPerfoInquiry;
//    private Job mockJob;
//    private Position mockPosition;
//    private Department mockDepartment;
//
//    @BeforeEach
//    void setUp() {
//        // Mock Department
//        DepartmentCreateDTO deptDTO = new DepartmentCreateDTO(
//                1L,
//                "인사부",
//                "인사 관리 및 채용",
//                "010-1234-5678",
//                null,
//                null
//        );
//
//        mockDepartment = Department.builder()
//                .createDTO(deptDTO)
//                .manageDept(null)
//                .pic(null)
//                .build();
//
//        // Mock Job
//        JobCreateDTO jobDTO = new JobCreateDTO(
//                1L,
//                "채용 담당자",
//                "신입 및 경력 사원 채용 관리",
//                Status.ACTIVE
//        );
//
//        mockJob = Job.builder()
//                .createDTO(jobDTO)
//                .dept(mockDepartment)
//                .build();
//
//        // Mock Position
//        PositionCreateDTO posDTO = new PositionCreateDTO(
//                "인턴",
//                1
//        );
//
//        mockPosition = Position.builder()
//                .positionCreateDTO(posDTO)
//                .build();
//
//        // Mock Employee (평가자)
//        mockEmp = Employee.builder()
//                .registerDTO(new EmployeeCreateDTO(
//                        "EMP001",
//                        1L,
//                        1L,
//                        1L,
//                        "alex",
//                        "female",
//                        "123423-2342133",
//                        500L,
//                        "123 Main St",
//                        "123423-2342133",
//                        "2142343@example.com",
//                        LocalDate.of(2024, 12, 31)
//                ))
//                .position(mockPosition)
//                .job(mockJob)
//                .department(mockDepartment)
//                .build();
//
//        // Mock Employee (평가 받은 사람)
//        mockPerfoedEmp = Employee.builder()
//                .registerDTO(new EmployeeCreateDTO(
//                        "EMP002",
//                        1L,
//                        1L,
//                        1L,
//                        "john",
//                        "male",
//                        "123423-2342134",
//                        600L,
//                        "456 Main St",
//                        "123423-2342134",
//                        "2142344@example.com",
//                        LocalDate.of(2024, 12, 31)
//                ))
//                .position(mockPosition)
//                .job(mockJob)
//                .department(mockDepartment)
//                .build();
//
//        // Mock Weight
//        mockWeight = Weight.builder()
//                .personalWeight(20L)
//                .teamWeight(20L)
//                .colWeight(20L)
//                .downwardWeight(20L)
//                .attendanceWeight(20L)
//                .build();
//
//        // Mock HrPerfo
//        mockHrPerfo = HrPerfo.builder()
//                .emp(mockEmp)
//                .score(85.5)
//                .status(HrPerfoStatus.WAIT)
//                .build();
//
//        // Mock HrPerfoInquiry
//        mockHrPerfoInquiry = HrPerfoInquiry.builder()
//                .hrPerfo(mockHrPerfo)
//                .reason("Disagreement on score")
//                .status(PassStatus.ACTIVE)
//                .build();
//    }
//
//    // Helper method to create UpdateInquiryRequestDTO
//    private UpdateInquiryRequestDTO createUpdateInquiryRequestDTO() {
//        return UpdateInquiryRequestDTO.builder()
//                .score(25.0)
//                .reason("Reviewed and approved.")
//                .build();
//    }
//
//    // Test Cases
//
//    // createHumanResource Tests
//    @Test
//    void testCreateHumanResource_Success() {
//        // Arrange
//        try (MockedStatic<PerformanceMapper> mockedMapper = mockStatic(PerformanceMapper.class)) {
//            mockedMapper.when(() -> PerformanceMapper.finalHrPerfo(eq(mockEmp), anyDouble()))
//                    .thenReturn(mockHrPerfo);
//
//            // Mocking repository methods
//            when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//            when(weightCommandRepository.findWeightByDeptId(anyLong())).thenReturn(mockWeight);
//
//            // Mocking KPI data with required conditions
//            Kpi mockKpi = Kpi.builder()
//                    .emp(mockEmp)
//                    .goal("목표1")
//                    .personalType(PersonalType.PERSONAL)
//                    .goalValueUnit("%")
//                    .goalValue(100L)
//                    .currentValue(90.0)
//                    .status(KpiCurrentStatus.WAIT)
//                    .goalDetail("이유1")
//                    .period("2024_YEAR")
//                    .build();
//
//            when(kpiCommandRepository.findByEmpId("EMP001")).thenReturn(Arrays.asList(mockKpi));
//            when(hrPerfoHistoryRepository.findByEmpId(anyString())).thenReturn(Arrays.asList());
//
//            when(hrPerfoCommandRepository.save(any(HrPerfo.class))).thenReturn(mockHrPerfo);
//
//            // Act & Assert
//            assertDoesNotThrow(() -> humanResourceCommandService.createHumanResource("EMP001"));
//
//            // Verify interactions
//            verify(employeeCommandRepository, times(1)).findById("EMP001");
//            verify(weightCommandRepository, times(1)).findWeightByDeptId(anyLong());
//            verify(kpiCommandRepository, times(1)).findByEmpId("EMP001");
//            verify(hrPerfoHistoryRepository, times(1)).findByEmpId("EMP001");
//            verify(hrPerfoCommandRepository, times(1)).save(mockHrPerfo);
//            mockedMapper.verify(() -> PerformanceMapper.finalHrPerfo(mockEmp, anyDouble()), times(1));
//        }
//    }
//
//
//    @Test
//    void testCreateHumanResource_EmpNotFound() {
//        // Arrange
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.createHumanResource("EMP001")
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(weightCommandRepository, times(0)).findWeightByDeptId(anyLong());
//        verify(kpiCommandRepository, times(0)).findByEmpId(anyString());
//        verify(hrPerfoHistoryRepository, times(0)).findByEmpId(anyString());
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    // updateHumanResource Tests
//    @Test
//    void testUpdateHumanResource_Success() {
//        // Arrange
//        Double newScore = 90.0;
//
//        // Mocking repository methods
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoCommandRepository.findByhrPerfoId(mockHrPerfo.getHrPerfoId())).thenReturn(Optional.of(mockHrPerfo));
//        when(hrPerfoCommandRepository.save(any(HrPerfo.class))).thenReturn(mockHrPerfo);
//
//        // Act & Assert
//        assertDoesNotThrow(() -> humanResourceCommandService.updateHumanResource("EMP001", newScore));
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(mockHrPerfo.getHrPerfoId());
//        verify(hrPerfoCommandRepository, times(1)).save(mockHrPerfo);
//    }
//
//    @Test
//    void testUpdateHumanResource_EmpNotFound() {
//        // Arrange
//        Double newScore = 90.0;
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHumanResource("EMP001", newScore)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoCommandRepository, times(0)).findByhrPerfoId(anyLong());
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    @Test
//    void testUpdateHumanResource_HrPerfoNotFound() {
//        // Arrange
//        Double newScore = 90.0;
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoCommandRepository.findByhrPerfoId(mockHrPerfo.getHrPerfoId())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHumanResource("EMP001", newScore)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_HRPERFO, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(mockHrPerfo.getHrPerfoId());
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    // createHrPerfoInquiry Tests
//    @Test
//    void testCreateHrPerfoInquiry_Success() {
//        // Arrange
//        Long hrperfoId = 100L;
//        String reason = "Disagreement on evaluation";
//
//        // Mocking repository methods
//        when(hrPerfoCommandRepository.findByhrPerfoId(hrperfoId)).thenReturn(Optional.of(mockHrPerfo));
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//
//        // Mocking PerformanceMapper.createhrPerfoInquiry
//        HrPerfoInquiry newInquiry = HrPerfoInquiry.builder()
//                .hrPerfo(mockHrPerfo)
//                .reason(reason)
//                .status(PassStatus.ACTIVE)
//                .build();
//
//        try (MockedStatic<PerformanceMapper> mockedMapper = mockStatic(PerformanceMapper.class)) {
//            mockedMapper.when(() -> PerformanceMapper.createhrPerfoInquiry(eq(mockHrPerfo), eq(reason)))
//                    .thenReturn(newInquiry);
//
//            when(hrPerfoInquiryRepository.save(any(HrPerfoInquiry.class))).thenReturn(newInquiry);
//
//            // Act & Assert
//            assertDoesNotThrow(() -> humanResourceCommandService.createHrPerfoInquiry("EMP001", hrperfoId, reason));
//
//            // Verify interactions
//            verify(employeeCommandRepository, times(1)).findById("EMP001");
//            verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(hrperfoId);
//            verify(hrPerfoInquiryRepository, times(1)).save(newInquiry);
//            mockedMapper.verify(() -> PerformanceMapper.createhrPerfoInquiry(mockHrPerfo, reason), times(1));
//        }
//    }
//
//    @Test
//    void testCreateHrPerfoInquiry_EmpNotFound() {
//        // Arrange
//        Long hrperfoId = 100L;
//        String reason = "Disagreement on evaluation";
//
//        when(hrPerfoCommandRepository.findByhrPerfoId(hrperfoId)).thenReturn(Optional.of(mockHrPerfo));
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.createHrPerfoInquiry("EMP001", hrperfoId, reason)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(hrperfoId);
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//    }
//
//    @Test
//    void testCreateHrPerfoInquiry_HrPerfoNotFound() {
//        // Arrange
//        Long hrperfoId = 100L;
//        String reason = "Disagreement on evaluation";
//
//        when(hrPerfoCommandRepository.findByhrPerfoId(hrperfoId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.createHrPerfoInquiry("EMP001", hrperfoId, reason)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_HRPERFO, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(0)).findById(anyString());
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(hrperfoId);
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//    }
//
//    @Test
//    void testCreateHrPerfoInquiry_WriterMismatch() {
//        // Arrange
//        Long hrperfoId = 100L;
//        String reason = "Disagreement on evaluation";
//
//        // hrPerfo's empId is different from empId of the inquiry creator
//        HrPerfo hrPerfoWithDifferentEmp = HrPerfo.builder()
//                .emp(mockPerfoedEmp) // Different employee
//                .score(75.0)
//                .status(HrPerfoStatus.WAIT)
//                .build();
//
//        when(hrPerfoCommandRepository.findByhrPerfoId(hrperfoId)).thenReturn(Optional.of(hrPerfoWithDifferentEmp));
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.createHrPerfoInquiry("EMP001", hrperfoId, reason)
//        );
//
//        assertEquals(ErrorCode.NOT_MATCH_WRITER, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(hrperfoId);
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//    }
//
//    // updateHrPerfoInquiry (Approve) Tests
//    @Test
//    void testUpdateHrPerfoInquiry_Approve_Success() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        UpdateInquiryRequestDTO updateInquiryRequestDTO = createUpdateInquiryRequestDTO();
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrperfoInquiryId)).thenReturn(Optional.of(mockHrPerfoInquiry));
//        when(hrPerfoCommandRepository.findByhrPerfoId(mockHrPerfoInquiry.getHrPerfo().getHrPerfoId())).thenReturn(Optional.of(mockHrPerfo));
//
//        when(hrPerfoInquiryRepository.save(any(HrPerfoInquiry.class))).thenReturn(mockHrPerfoInquiry);
//        when(hrPerfoCommandRepository.save(any(HrPerfo.class))).thenReturn(mockHrPerfo);
//
//        // Act & Assert
//        assertDoesNotThrow(() -> humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, updateInquiryRequestDTO));
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(1)).findByhrPerfoInquiryId(hrperfoInquiryId);
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(mockHrPerfoInquiry.getHrPerfo().getHrPerfoId());
//        verify(hrPerfoInquiryRepository, times(1)).save(mockHrPerfoInquiry);
//        verify(hrPerfoCommandRepository, times(1)).save(mockHrPerfo);
//    }
//
//    @Test
//    void testUpdateHrPerfoInquiry_Approve_EmpNotFound() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        UpdateInquiryRequestDTO updateInquiryRequestDTO = createUpdateInquiryRequestDTO();
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, updateInquiryRequestDTO)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(0)).findByhrPerfoInquiryId(anyLong());
//        verify(hrPerfoCommandRepository, times(0)).findByhrPerfoId(anyLong());
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    @Test
//    void testUpdateHrPerfoInquiry_Approve_HrPerfoInquiryNotFound() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        UpdateInquiryRequestDTO updateInquiryRequestDTO = createUpdateInquiryRequestDTO();
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrperfoInquiryId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, updateInquiryRequestDTO)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_HRPERFOINQUIRY, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(1)).findByhrPerfoInquiryId(hrperfoInquiryId);
//        verify(hrPerfoCommandRepository, times(0)).findByhrPerfoId(anyLong());
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    // updateHrPerfoInquiry (Reject) Tests
//    @Test
//    void testUpdateHrPerfoInquiry_Reject_Success() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        String reason = "Insufficient evidence";
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrperfoInquiryId)).thenReturn(Optional.of(mockHrPerfoInquiry));
//
//        when(hrPerfoInquiryRepository.save(any(HrPerfoInquiry.class))).thenReturn(mockHrPerfoInquiry);
//
//        // Act & Assert
//        assertDoesNotThrow(() -> humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, reason));
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(1)).findByhrPerfoInquiryId(hrperfoInquiryId);
//        verify(hrPerfoInquiryRepository, times(1)).save(mockHrPerfoInquiry);
//        verify(hrPerfoCommandRepository, times(0)).save(any(HrPerfo.class));
//    }
//
//    @Test
//    void testUpdateHrPerfoInquiry_Reject_EmpNotFound() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        String reason = "Insufficient evidence";
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, reason)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(0)).findByhrPerfoInquiryId(anyLong());
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//    }
//
//    @Test
//    void testUpdateHrPerfoInquiry_Reject_HrPerfoInquiryNotFound() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        String reason = "Insufficient evidence";
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrperfoInquiryId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () ->
//                humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, reason)
//        );
//
//        assertEquals(ErrorCode.NOT_FOUND_HRPERFOINQUIRY, exception.getErrorCode());
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(1)).findByhrPerfoInquiryId(hrperfoInquiryId);
//        verify(hrPerfoInquiryRepository, times(0)).save(any(HrPerfoInquiry.class));
//    }
//
//    // Additional Tests for createHumanResource (Final Score Calculation)
//    @Test
//    void testCreateHumanResource_FinalScoreCalculation() {
//        // Arrange
//        // Mocking PerformanceMapper.finalHrPerfo
//        HrPerfo calculatedHrPerfo = HrPerfo.builder()
//                .emp(mockEmp)
//                .score(90.0)
//                .status(HrPerfoStatus.WAIT)
//                .build();
//
//        try (MockedStatic<PerformanceMapper> mockedMapper = mockStatic(PerformanceMapper.class)) {
//            mockedMapper.when(() -> PerformanceMapper.finalHrPerfo(eq(mockEmp), anyDouble()))
//                    .thenReturn(calculatedHrPerfo);
//
//            // Mocking repository methods
//            when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//            when(weightCommandRepository.findWeightByDeptId(anyLong())).thenReturn(mockWeight);
//
//            // Mocking KPI and HrPerfoHistory
//            Kpi mockKpi = Kpi.builder()
//                    .emp(mockEmp)
//                    .goal("목표1")
//                    .personalType(PersonalType.PERSONAL)
//                    .goalValueUnit("%")
//                    .goalValue(100L)
//                    .currentValue(90.0)
//                    .status(KpiCurrentStatus.WAIT)
//                    .goalDetail("이유1")
//                    .period("2024_YEAR")
//                    .build();
//
//            Kpi mockTeamKpi = Kpi.builder()
//                    .emp(mockEmp)
//                    .goal("목표2")
//                    .personalType(PersonalType.TEAM)
//                    .goalValueUnit("%")
//                    .goalValue(100L)
//                    .currentValue(90.0)
//                    .status(KpiCurrentStatus.WAIT)
//                    .goalDetail("이유2")
//                    .period("2024_YEAR")
//                    .build();
//
//            HrPerfoHistory mockHistory = HrPerfoHistory.builder()
//                    .perfoed_emp(mockPerfoedEmp)
//                    .perfo_emp(mockEmp)
//                    .adjustmentDegree(1L)
//                    .adjustmentColScore(8L)
//                    .adjustmentDownScore(75L)
//                    .reason("이유")
//                    .build();
//
//            when(kpiCommandRepository.findByEmpId("EMP001")).thenReturn(Arrays.asList(mockKpi, mockTeamKpi));
//            when(hrPerfoHistoryRepository.findByEmpId("EMP001")).thenReturn(Arrays.asList(mockHistory));
//
//            when(hrPerfoCommandRepository.save(any(HrPerfo.class))).thenReturn(calculatedHrPerfo);
//
//            // Act & Assert
//            assertDoesNotThrow(() -> humanResourceCommandService.createHumanResource("EMP001"));
//
//            // Verify interactions
//            verify(employeeCommandRepository, times(1)).findById("EMP001");
//            verify(weightCommandRepository, times(1)).findWeightByDeptId(anyLong());
//            verify(kpiCommandRepository, times(1)).findByEmpId("EMP001");
//            verify(hrPerfoHistoryRepository, times(1)).findByEmpId("EMP001");
//            verify(hrPerfoCommandRepository, times(1)).save(calculatedHrPerfo);
//            mockedMapper.verify(() -> PerformanceMapper.finalHrPerfo(mockEmp, anyDouble()), times(1));
//        }
//    }
//
//    // updateHrPerfoInquiry Tests (Approve) - Final Score Update
//    @Test
//    void testUpdateHrPerfoInquiry_Approve_FinalScoreUpdate() {
//        // Arrange
//        Long hrperfoInquiryId = 200L;
//        UpdateInquiryRequestDTO updateInquiryRequestDTO = createUpdateInquiryRequestDTO();
//
//        // Mocking HrPerfoInquiry and HrPerfo
//        HrPerfoInquiry hrPerfoInquiry = HrPerfoInquiry.builder()
//                .hrPerfo(mockHrPerfo)
//                .reason("Disagreement on evaluation")
//                .status(PassStatus.ACTIVE)
//                .build();
//
//        HrPerfo updatedHrPerfo = HrPerfo.builder()
//                .emp(mockEmp)
//                .score(95.0) // Updated score
//                .status(HrPerfoStatus.WAIT)
//                .build();
//
//        when(employeeCommandRepository.findById("EMP001")).thenReturn(Optional.of(mockEmp));
//        when(hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrperfoInquiryId)).thenReturn(Optional.of(hrPerfoInquiry));
//        when(hrPerfoCommandRepository.findByhrPerfoId(hrPerfoInquiry.getHrPerfo().getHrPerfoId())).thenReturn(Optional.of(mockHrPerfo));
//
//        // Mocking update methods
//        doNothing().when(hrPerfoInquiry).updateinquiry(any(Employee.class), any(UpdateInquiryRequestDTO.class));
//        doNothing().when(mockHrPerfo).updateHrPerfo(anyDouble());
//
//        when(hrPerfoInquiryRepository.save(hrPerfoInquiry)).thenReturn(hrPerfoInquiry);
//        when(hrPerfoCommandRepository.save(any(HrPerfo.class))).thenReturn(mockHrPerfo);
//
//        // Act & Assert
//        assertDoesNotThrow(() -> humanResourceCommandService.updateHrPerfoInquiry("EMP001", hrperfoInquiryId, updateInquiryRequestDTO));
//
//        // Verify interactions
//        verify(employeeCommandRepository, times(1)).findById("EMP001");
//        verify(hrPerfoInquiryRepository, times(1)).findByhrPerfoInquiryId(hrperfoInquiryId);
//        verify(hrPerfoCommandRepository, times(1)).findByhrPerfoId(mockHrPerfo.getHrPerfoId());
//        verify(hrPerfoInquiryRepository, times(1)).save(hrPerfoInquiry);
//        verify(hrPerfoCommandRepository, times(1)).save(mockHrPerfo);
//    }
//
//    // Additional Tests can be added as needed
//
//}
