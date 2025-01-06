package com.touchdown.perflowbackend.employee.query.service;


import com.touchdown.perflowbackend.authority.domain.aggregate.AuthType;
import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeUpdateRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeResponseList;

import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeQueryServiceTest {
    @Mock
    private EmployeeQueryRepository employeeRepository;
    // 실제 예시에서는 'EmployeeQueryRepository'일 수도 있음.

    @InjectMocks
    private EmployeeQueryService employeeQueryService;

    private Pageable pageable;
    private List<Employee> mockEmployeeList = new ArrayList<>();

    // 공통 given 값들
    private EmployeeCreateDTO create1DTO;
    private EmployeeCreateDTO create2DTO;
    private Department mockDepartment;
    private Position mockPosition;
    private Job mockJob;
    private Employee mockEmployee1;
    private Employee mockEmployee2;
    private Authority mockAuthority;
    private EmployeeUpdateRequestDTO updateRequest;


    @BeforeEach
    void setUp() {
        // 페이징 파라미터 (page=0, size=5)
        pageable = PageRequest.of(0, 5, Sort.by("empId").ascending());

        // 테스트에 사용할 DTO 샘플
        create1DTO = EmployeeCreateDTO.builder()
                .empId("EMP1111")
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
        // 테스트에 사용할 DTO 샘플
        create2DTO = EmployeeCreateDTO.builder()
                .empId("EMP2222")
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

        mockEmployee1 = EmployeeMapper.toEntity(create1DTO, mockPosition, mockJob, mockDepartment, create1DTO.getRrn());
        mockEmployee2 = EmployeeMapper.toEntity(create2DTO, mockPosition, mockJob, mockDepartment, create2DTO.getRrn());

        mockAuthority = new Authority();
        mockAuthority.setAuthorityId(1L);
        mockAuthority.setType(AuthType.EMPLOYEE); // 가정

        mockEmployeeList.add(mockEmployee1);
        mockEmployeeList.add(mockEmployee2);
    }

    @Test
    @DisplayName("getAllEmployees - 정상적으로 페이징 조회 시, EmployeeResponseList 반환")
    void testGetAllEmployees_success() {
        // given
        // 총 2개 데이터, 페이지 크기=5이므로 1페이지로 가정
        Page<Employee> mockPage = new PageImpl<>(mockEmployeeList, pageable, mockEmployeeList.size());

        // repository가 findAll(pageable) 호출 시, mockPage 반환
        given(employeeRepository.findAll(any(Pageable.class)))
                .willReturn(mockPage);

        // when
        EmployeeResponseList responseList = employeeQueryService.getAllEmployees(pageable);

        // then
        // repository 호출 검증
        then(employeeRepository).should(times(1)).findAll(any(Pageable.class));

        assertNotNull(responseList);
        List<EmployeeQueryResponseDTO> resultList = responseList.getEmployeeList();
        assertEquals(2, resultList.size());      // mockEmployeeList.size()

        // 첫 번째 항목 확인 (Mapper로 변환된 값)
        EmployeeQueryResponseDTO firstDto = resultList.get(0);
        assertEquals("EMP1111", firstDto.getEmpId());
        assertEquals("홍길동", firstDto.getName());

        // 페이징 정보 확인
        assertEquals(1, responseList.getTotalPages());        // 2건 / size=5 => totalPages=1
        assertEquals(2, responseList.getTotalItems());        // 총 2건
        assertEquals(1, responseList.getCurrentPage());       // page=0 이면 currentPage=1
        assertEquals(5, responseList.getPageSize());          // size=5
    }

    @Test
    @DisplayName("getAllEmployees - 데이터가 없을 때")
    void testGetAllEmployees_empty() {
        // given
        List<Employee> emptyList = new ArrayList<>();
        Page<Employee> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        given(employeeRepository.findAll(any(Pageable.class)))
                .willReturn(emptyPage);

        // when
        EmployeeResponseList responseList = employeeQueryService.getAllEmployees(pageable);

        // then
        assertNotNull(responseList);
        assertTrue(responseList.getEmployeeList().isEmpty());
        assertEquals(0, responseList.getTotalItems());
        assertEquals(1, responseList.getCurrentPage()); // page=0 => currentPage=1
        assertEquals(0, responseList.getTotalPages());
    }

}