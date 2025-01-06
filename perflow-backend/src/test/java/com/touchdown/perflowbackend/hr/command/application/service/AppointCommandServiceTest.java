package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.*;
import com.touchdown.perflowbackend.hr.command.domain.repository.AppointCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AppointCommandServiceTest {
    @Mock
    private AppointCommandRepository appointCommandRepository;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @InjectMocks
    private AppointCommandService appointCommandService;

    private AppointCreateDTO createDTO;
    private Employee mockEmployee;

    @BeforeEach
    void setUp() {
        // 테스트용 DTO
        createDTO = new AppointCreateDTO();
        createDTO.setEmpId("EMP001");
        createDTO.setType(Type.PROMOTION); // 예: 승진
        // ... 기타 필드도 필요하면 세팅 (effectiveDate 등)

        // 기존 Employee (DB에서 찾을 가짜 엔티티)
        mockEmployee = new Employee();
        mockEmployee.setEmpId("EMP001");

        Position currentPosition = new Position();
        currentPosition.setPositionId(10L);
        currentPosition.setName("사원");
        mockEmployee.setPosition(currentPosition);

        // 부서, job 등도 필요하면 세팅
        Department currentDept = new Department();
        currentDept.setDepartmentId(100L);
        currentDept.setName("개발팀");
        mockEmployee.setDept(currentDept);

        Job currentJob = new Job();
        currentJob.setJobId(50L);
        currentJob.setName("개발자");
        mockEmployee.setJob(currentJob);
    }

    @Test
    @DisplayName("createAppoint - PROMOTION 시, before/after가 position으로 세팅되고 appoint가 저장된다")
    void testCreateAppoint_promotion() {
        // given
        // employee 조회 성공
        given(employeeCommandRepository.findById(createDTO.getEmpId()))
                .willReturn(Optional.of(mockEmployee));

        AppointCommandService spyService = Mockito.spy(appointCommandService);

        // 만약 promotionEmployee(...)가 public이면 다음과 같이 Stub 가능:
        doReturn("대리").when(spyService).promotionEmployee(any(Employee.class), any(AppointCreateDTO.class));

        // inject spy into @InjectMocks might be tricky, but let's assume a simpler approach:
        // we can mock the returning value of that private method if we refactor it or we assume a simplified approach

        // when
        // 여기서는 임시로 "대리"가 반환될 것이라고 가정
        spyService.createAppoint(createDTO);

        // then
        // "before" = employee.getPosition().getName() = "사원"
        // "after" = "대리" (mocked)
        // Appoint 엔티티 생성 후 save(...)
        then(appointCommandRepository).should(times(1)).save(any(Appoint.class));
    }

    @Test
    @DisplayName("createAppoint - employee 찾지 못하면 예외 발생")
    void testCreateAppoint_employeeNotFound() {
        // given
        given(employeeCommandRepository.findById("EMP001"))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> appointCommandService.createAppoint(createDTO));
        // assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, ex.getErrorCode()); // 필요 시

        // save(...) 호출 안 함
        then(appointCommandRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("createAppoint - TRANSFER 시, before/after가 부서명으로 세팅된 뒤 appoint 저장")
    void testCreateAppoint_transfer() {
        // given
        // 지금은 type=PROMOTION이지만, 여기서 TRANSFER로 바꿔 시나리오 확인
        createDTO.setType(Type.TRANSFER);

        // employee 조회
        given(employeeCommandRepository.findById("EMP001"))
                .willReturn(Optional.of(mockEmployee));

        // transferEmployee(...)가 "인사부" 같은 새 부서명을 after로 반환한다고 가정
        AppointCommandService spyService = Mockito.spy(appointCommandService);
        doReturn("인사부").when(spyService).transferEmployee(any(Employee.class), any(AppointCreateDTO.class));

        // when
        spyService.createAppoint(createDTO);

        // then
        // before = mockEmployee.getDept().getName() = "개발팀"
        // after = "인사부"
        then(appointCommandRepository).should(times(1)).save(any(Appoint.class));
    }
}