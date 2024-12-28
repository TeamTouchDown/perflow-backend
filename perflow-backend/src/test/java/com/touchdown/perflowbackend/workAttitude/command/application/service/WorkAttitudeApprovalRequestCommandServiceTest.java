/*
package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeApprovalRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeApprovalRequestCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeApprovalMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeApprovalRequestCommandServiceTest {

    @Mock
    private WorkAttitudeApprovalRequestCommandRepository approvalRequestRepository;

    @Mock
    private EmployeeCommandRepository employeeRepository;

    @InjectMocks
    private WorkAttitudeApprovalRequestCommandService commandService;

    @BeforeEach
    void setUp() {
        // 만약 static으로 SecurityContext(또는 EmployeeUtil)에서 empId를 반환받는다면
        // Mockito.mockStatic() 같은 방식으로 처리해야 할 수도 있음
        // 간단히 아래처럼 스텁(stub)을 줄 수도 있음:
        mockStaticEmployeeUtil("testEmp");
    }

    */
/**
     * EmployeeUtil.getEmpId()을 mocking하는 헬퍼 메서드
     *//*

    private void mockStaticEmployeeUtil(String returnEmpId) {
        // Mockito 3.4+ 에서는 mockStatic 사용 가능
        // EmployeeUtil이 static 메서드를 쓴다면 이런 식으로:
        // MockedStatic<EmployeeUtil> employeeUtilMock = Mockito.mockStatic(EmployeeUtil.class);
        // employeeUtilMock.when(EmployeeUtil::getEmpId).thenReturn(returnEmpId);

        // 또는 일반적인 메서드라면 아래와 같이 가능
        doReturn(returnEmpId).when(employeeUtil).getEmpId();
    }

    @Test
    void registerAnnual_Success() {
        // given
        String currentEmpId = "testEmp";
        Employee currentEmp = Employee.builder()
                .empId(currentEmpId)
                .joinDate(LocalDate.now().minusYears(2)) // 입사 2년차라 가정
                .build();

        String approverEmpId = "approverEmp";
        Employee approver = Employee.builder()
                .empId(approverEmpId)
                .build();

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId(approverEmpId);           // 결재자 ID
        dto.setAnnualStart(LocalDateTime.now().plusDays(1));
        dto.setAnnualEnd(LocalDateTime.now().plusDays(2));
        dto.setRequestType(RequestType.ANNUAL);

        // 스텁 설정
        // 현재 로그인 사용자 조회
        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.of(currentEmp));

        // 결재자 조회
        given(employeeRepository.findById(approverEmpId)).willReturn(Optional.of(approver));

        // 날짜 중복 & 연차개수 검증 부분에서 호출
        // existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween 반환값 false -> 중복 없다고 가정
        given(approvalRequestRepository.existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
                anyString(), any(RequestType.class), any(Status.class), any(DeleteStatus.class),
                any(LocalDateTime.class), any(LocalDateTime.class)
        )).willReturn(false);

        // 사용한 연차 조회 (countByEmpIdAndRequestTypeAndStatus)
        given(approvalRequestRepository.countByEmpIdAndRequestTypeAndStatus(
                currentEmpId, RequestType.ANNUAL, Status.CONFIRMED
        )).willReturn(0L);

        // when
        assertDoesNotThrow(() -> commandService.registerAnnual(dto));

        // then
        // 한 번 이상 save 되었는지 검증
        verify(approvalRequestRepository, times(1)).save(any(ApprovalRequest.class));
    }

    @Test
    void registerAnnual_WhenEmployeeNotFound_ThrowsException() {
        // given
        String currentEmpId = "testEmp";
        // currentEmpId 조회가 안된다고 가정
        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.empty());

        // when & then
        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp");
        dto.setAnnualStart(LocalDateTime.now());
        dto.setAnnualEnd(LocalDateTime.now());

        // CustomException(ErrorCode.NOT_FOUND_EMPLOYEE) 예상
        CustomException ex = assertThrows(CustomException.class,
                () -> commandService.registerAnnual(dto));
        assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, ex.getErrorCode());
    }

    @Test
    void registerAnnual_WhenApproverNotFound_ThrowsException() {
        // given
        String currentEmpId = "testEmp";
        Employee currentEmp = Employee.builder()
                .empId(currentEmpId)
                .joinDate(LocalDate.now())
                .build();
        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.of(currentEmp));

        // approver(empId="approverEmp") 가 없다고 가정
        given(employeeRepository.findById("approverEmp")).willReturn(Optional.empty());

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp");
        dto.setAnnualStart(LocalDateTime.now());
        dto.setAnnualEnd(LocalDateTime.now());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> commandService.registerAnnual(dto));
        assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, ex.getErrorCode());
    }

    @Test
    void registerAnnual_WhenDuplicateDates_ThrowsException() {
        // given
        String currentEmpId = "testEmp";
        Employee currentEmp = Employee.builder()
                .empId(currentEmpId)
                .joinDate(LocalDate.now())
                .build();
        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.of(currentEmp));

        Employee approver = Employee.builder()
                .empId("approverEmp")
                .build();
        given(employeeRepository.findById("approverEmp")).willReturn(Optional.of(approver));

        // 날짜 중복이 있다고 가정
        given(approvalRequestRepository.existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
                eq(currentEmpId), any(RequestType.class), eq(Status.PENDING), eq(DeleteStatus.ACTIVATED),
                any(LocalDateTime.class), any(LocalDateTime.class)
        )).willReturn(true);

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp");
        dto.setAnnualStart(LocalDateTime.now());
        dto.setAnnualEnd(LocalDateTime.now());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> commandService.registerAnnual(dto));
        assertEquals(ErrorCode.DUPLICATE_ANNUAL, ex.getErrorCode());
    }

    @Test
    void registerAnnual_WhenNotEnoughAnnual_ThrowsException() {
        // given
        String currentEmpId = "testEmp";
        Employee currentEmp = Employee.builder()
                .empId(currentEmpId)
                // 입사 1년차 가정
                .joinDate(LocalDate.now().minusYears(1))
                .build();
        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.of(currentEmp));

        Employee approver = Employee.builder()
                .empId("approverEmp")
                .build();
        given(employeeRepository.findById("approverEmp")).willReturn(Optional.of(approver));

        // 날짜 중복 없음
        given(approvalRequestRepository.existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
                anyString(), any(RequestType.class), eq(Status.PENDING), eq(DeleteStatus.ACTIVATED),
                any(LocalDateTime.class), any(LocalDateTime.class)
        )).willReturn(false);

        // 이미 사용한 연차가 매우 많다고 가정 (예: 14일 사용)
        given(approvalRequestRepository.countByEmpIdAndRequestTypeAndStatus(
                currentEmpId, RequestType.ANNUAL, Status.CONFIRMED
        )).willReturn(14L);

        // 그리고 이번에 3일을 신청 -> 총 17일
        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp");
        dto.setAnnualStart(LocalDateTime.now().plusDays(1));
        dto.setAnnualEnd(LocalDateTime.now().plusDays(3));

        // 기본 연차가 15일이므로, (14 + 3) > 15 => 에러 발생
        CustomException ex = assertThrows(CustomException.class,
                () -> commandService.registerAnnual(dto));
        assertEquals(ErrorCode.NOT_ENOUGH_ANNUAL, ex.getErrorCode());
    }

    @Test
    void registerAnnual_SavesApprovalRequestProperly() {
        // given
        String currentEmpId = "testEmp";
        Employee currentEmp = Employee.builder()
                .empId(currentEmpId)
                .joinDate(LocalDate.now().minusYears(2))
                .build();
        Employee approver = Employee.builder()
                .empId("approverEmp")
                .build();

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp");
        dto.setAnnualStart(LocalDateTime.now().plusDays(1));
        dto.setAnnualEnd(LocalDateTime.now().plusDays(1));
        dto.setRequestType(RequestType.ANNUAL);

        given(employeeRepository.findById(currentEmpId)).willReturn(Optional.of(currentEmp));
        given(employeeRepository.findById("approverEmp")).willReturn(Optional.of(approver));
        // 중복 없음
        given(approvalRequestRepository.existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
                anyString(), any(RequestType.class), any(Status.class), any(DeleteStatus.class),
                any(LocalDateTime.class), any(LocalDateTime.class)
        )).willReturn(false);

        // 이미 사용한 연차 = 0
        given(approvalRequestRepository.countByEmpIdAndRequestTypeAndStatus(
                currentEmpId, RequestType.ANNUAL, Status.CONFIRMED
        )).willReturn(0L);

        // when
        commandService.registerAnnual(dto);

        // then
        // 실제로 저장되는 ApprovalRequest를 캡처해서 검증
        ArgumentCaptor<ApprovalRequest> captor = ArgumentCaptor.forClass(ApprovalRequest.class);
        verify(approvalRequestRepository).save(captor.capture());

        ApprovalRequest savedRequest = captor.getValue();
        assertNotNull(savedRequest);
        assertEquals(currentEmp, savedRequest.getEmpId());
        assertEquals(approver, savedRequest.getApproverId());
        assertEquals(RequestType.ANNUAL, savedRequest.getRequestType());
        assertEquals(Status.PENDING, savedRequest.getStatus());
    }


    @Test
    void approveAnnual_Success() {
        // given
        Long annualId = 1L;
        ApprovalRequest existingRequest = new ApprovalRequest();
        existingRequest.setRequestId(annualId);
        existingRequest.setStatus(Status.PENDING);

        given(approvalRequestRepository.findById(annualId)).willReturn(Optional.of(existingRequest));

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        // dto 안에 딱히 중요한 내용 없어도 approve에는 status만 CONFIRMED로 바꿔버림

        // when
        commandService.approveAnnual(annualId, dto);

        // then
        assertEquals(Status.CONFIRMED, existingRequest.getStatus());
        verify(approvalRequestRepository, times(1)).save(existingRequest);
    }

    @Test
    void approveAnnual_WhenNotFound_ThrowsException() {
        // given
        Long annualId = 999L;
        given(approvalRequestRepository.findById(annualId)).willReturn(Optional.empty());

        // when & then
        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        assertThrows(IllegalArgumentException.class,
                () -> commandService.approveAnnual(annualId, dto));
    }

    @Test
    void rejectAnnual_Success() {
        // given
        Long annualId = 1L;
        ApprovalRequest existingRequest = new ApprovalRequest();
        existingRequest.setRequestId(annualId);
        existingRequest.setStatus(Status.PENDING);

        given(approvalRequestRepository.findById(annualId)).willReturn(Optional.of(existingRequest));

        // when
        commandService.rejectAnnual(annualId, "이 사유로 반려합니다.");

        // then
        assertEquals(Status.REJECTED, existingRequest.getStatus());
        assertEquals("이 사유로 반려합니다.", existingRequest.getRejectReason());
        verify(approvalRequestRepository, times(1)).save(existingRequest);
    }

    @Test
    void rejectAnnual_WhenNotFound_ThrowsException() {
        // given
        Long annualId = 999L;
        given(approvalRequestRepository.findById(annualId)).willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> commandService.rejectAnnual(annualId, "사유"));
    }

    // ----------------------------------------------------------
    // 다른 메서드(Overtime, Vacation, Travel 등)에 대해서도
    // register, update, delete, approve, reject 흐름을 유사하게 작성해주시면 됩니다.
    // ----------------------------------------------------------

    @Test
    void requestTravel_WhenApproverNotFound_ThrowsException() {
        // given
        Employee currentEmp = Employee.builder()
                .empId("testEmp")
                .joinDate(LocalDate.now())
                .build();
        given(employeeRepository.findById("testEmp")).willReturn(Optional.of(currentEmp));

        // approver 조회 실패
        given(employeeRepository.findById("approverEmp")).willReturn(Optional.empty());

        WorkAttitudeApprovalRequestDTO dto = new WorkAttitudeApprovalRequestDTO();
        dto.setEmpId("approverEmp"); // 결재자
        dto.setRequestType(RequestType.TRAVEL);

        // when & then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> commandService.requestTravel(dto));
        assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE.getMessage(), ex.getReason());
    }

}
*/
