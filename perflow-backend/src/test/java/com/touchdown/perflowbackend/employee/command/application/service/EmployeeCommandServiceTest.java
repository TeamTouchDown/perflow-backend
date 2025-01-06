package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.authority.domain.aggregate.AuthType;
import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityEmployeeRepository;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeUpdateRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import com.touchdown.perflowbackend.security.util.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeCommandServiceTest {

    @Mock
    private DepartmentCommandRepository departmentCommandRepository;

    @Mock
    private PositionCommandRepository positionCommandRepository;

    @Mock
    private JobCommandRepository jobCommandRepository;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private AuthorityEmployeeRepository authorityEmployeeRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @Mock
    private EntityManager entityManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmployeeCommandService employeeCommandService;
    // ↑ createEmployee(...) 메서드가 들어있는 Service 클래스의 이름을 맞춰주세요.

    // 공통 given 값들
    private EmployeeCreateDTO createDTO;
    private Department mockDepartment;
    private Position mockPosition;
    private Job mockJob;
    private Employee mockEmployee;
    private Authority mockAuthority;
    private EmployeeUpdateRequestDTO updateRequest;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 DTO 샘플
        createDTO = EmployeeCreateDTO.builder()
                .empId("EMP9999")
                .positionId(3L)
                .jobId(5L)
                .departmentId(10L)
                .name("홍길동")
                .gender("Male")
                .rrn("8001011234567")  // 앞 6자리 + 뒤 7자리
                .pay(3000000L)
                .address("서울시 강남구")
                .contact("010-9999-9999")
                .email("[email protected]")
                .joinDate(LocalDate.of(2023, 1, 1))
                .build();



        // 테스트용 DTO
        updateRequest = new EmployeeUpdateRequestDTO(
                "EMP9999",         // empId
                "홍길동-수정",      // name
                4000000L,          // pay
                "서울시 서초구",    // address
                "010-0000-1111",   // contact
                "[email protected]", // email
                LocalDate.of(2023, 7, 15)
        );

        // Mock 리턴용 엔티티들
        mockDepartment = new Department();
        mockDepartment.setDepartmentId(10L);
        mockDepartment.setName("자금팀");

        mockPosition = new Position();
        mockPosition.setPositionId(3L);
        mockPosition.setName("부장");

        mockJob = new Job();
        mockJob.setJobId(5L);
        mockJob.setName("투자분석가");

        mockEmployee = EmployeeMapper.toEntity(createDTO, mockPosition, mockJob, mockDepartment, createDTO.getRrn());

        mockAuthority = new Authority();
        mockAuthority.setAuthorityId(1L);
        mockAuthority.setType(AuthType.EMPLOYEE); // 가정
    }

    @Test
    @DisplayName("createEmployee - 성공 케이스")
    void testCreateEmployee_success() throws Exception {
        // given
        given(departmentCommandRepository.findById(createDTO.getDepartmentId()))
                .willReturn(java.util.Optional.of(mockDepartment));
        given(positionCommandRepository.findById(createDTO.getPositionId()))
                .willReturn(java.util.Optional.of(mockPosition));
        given(jobCommandRepository.findById(createDTO.getJobId()))
                .willReturn(java.util.Optional.of(mockJob));

        given(encryptionUtil.encrypt("1234567"))
                .willReturn("ENCRYPTED_1234567");

        // Authority 리턴
        given(authorityRepository.findByType(AuthType.EMPLOYEE))
                .willReturn(mockAuthority);

        given(employeeCommandRepository.save(any(Employee.class)))
                .willReturn(mockEmployee);
        given(jwtTokenProvider.createEmailToken(anyString(), anyMap()))
                .willReturn("MOCKED_TOKEN_VALUE");

        // when
        employeeCommandService.createEmployee(createDTO);

        // then
        // 예외 발생 없이 정상 종료되면 OK
        // 필요한 경우 특정 메서드 호출 여부나 파라미터를 검증할 수 있음
        then(departmentCommandRepository).should(times(1))
                .findById(eq(10L));
        then(positionCommandRepository).should(times(1))
                .findById(eq(3L));
        then(jobCommandRepository).should(times(1))
                .findById(eq(5L));

        then(encryptionUtil).should(times(1))
                .encrypt("1234567");

        then(entityManager).should(times(1))
                .persist(any(Employee.class));
        then(employeeCommandRepository).should(times(1))
                .save(any(Employee.class));

        then(authorityRepository).should(times(1))
                .findByType(AuthType.EMPLOYEE);
        then(authorityEmployeeRepository).should(times(1))
                .save(any(AuthorityEmployee.class));
    }

    @Test
    @DisplayName("createEmployee - 부서를 찾을 수 없을 때 예외 발생")
    void testCreateEmployee_notFoundDepartment() {
        // given
        given(departmentCommandRepository.findById(anyLong()))
                .willReturn(java.util.Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> employeeCommandService.createEmployee(createDTO)
        );

        // 예외 메시지 혹은 코드 검증
        assertEquals(ErrorCode.NOT_FOUND_DEPARTMENT, ex.getErrorCode());

        // 아래 호출들은 일어나지 않아야 함(Department 못 찾았으니 바로 예외)
        then(positionCommandRepository).should(never()).findById(anyLong());
        then(jobCommandRepository).should(never()).findById(anyLong());
        then(encryptionUtil).shouldHaveNoInteractions();
        then(entityManager).shouldHaveNoInteractions();
        then(employeeCommandRepository).shouldHaveNoInteractions();
        then(authorityRepository).shouldHaveNoInteractions();
        then(authorityEmployeeRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("updateEmployee - 정상적으로 업데이트")
    void testUpdateEmployee_success() {
        // given
        given(employeeCommandRepository.findById(updateRequest.getEmpId()))
                .willReturn(Optional.of(mockEmployee));

        // when
        employeeCommandService.updateEmployee(updateRequest);

        // then
        // 실제로 엔티티 필드가 업데이트 되었는지 확인
        assertEquals("홍길동-수정", mockEmployee.getName());
        assertEquals(4000000L, mockEmployee.getPay());
        assertEquals("서울시 서초구", mockEmployee.getAddress());
        assertEquals("010-0000-1111", mockEmployee.getContact());
        assertEquals("[email protected]", mockEmployee.getEmail());
        assertEquals(LocalDate.of(2023, 7, 15), mockEmployee.getJoinDate());

        then(employeeCommandRepository).should(times(1)).save(mockEmployee);
    }

    @Test
    @DisplayName("updateEmployee - 존재하지 않는 사원 empId일 경우 예외 발생")
    void testUpdateEmployee_notFoundEmployee() {
        // given
        given(employeeCommandRepository.findById(updateRequest.getEmpId()))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> employeeCommandService.updateEmployee(updateRequest)
        );

        then(employeeCommandRepository).should(never()).save(any(Employee.class));
    }

}