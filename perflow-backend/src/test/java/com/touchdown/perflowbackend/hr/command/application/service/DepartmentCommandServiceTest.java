package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.DepartmentMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Pic;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PicCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DepartmentCommandServiceTest {

    @Mock
    private DepartmentCommandRepository departmentCommandRepository;

    @Mock
    private PicCommandRepository picCommandRepository;

    @Mock
    private EmployeeCommandRepository employeeCommandRepository;

    @InjectMocks
    private DepartmentCommandService departmentCommandService;

    private DepartmentCreateDTO createDTO;
    private Department managedDepartment;
    private Employee picEmployee;
    private Employee picEmployee2;

    private DepartmentUpdateDTO updateDTO;
    private Department existingDepartment;

    @BeforeEach
    void setUp() {
        // 테스트용 DTO
        createDTO = new DepartmentCreateDTO();
        createDTO.setManageDeptId(1L);
        createDTO.setName("마케팅부");
        createDTO.setResponsibility("마케팅 및 홍보");
        createDTO.setContact("02-1234-5678");
        createDTO.setPicId("EMP001");

        // 상위 부서 (예: 본사)
        managedDepartment = new Department();
        managedDepartment.setDepartmentId(1L);
        managedDepartment.setName("본사");

        // 담당자 (사원)
        picEmployee = new Employee();
        picEmployee.setEmpId("EMP001");
        picEmployee.setName("김사원");

        // 1) 업데이트 DTO
        updateDTO = new DepartmentUpdateDTO();
        updateDTO.setManageDeptId(2L); // 상위 부서 ID
        updateDTO.setName("마케팅부");
        updateDTO.setResponsibility("마케팅 기획 및 운영");
        updateDTO.setContact("02-1234-5678");
        updateDTO.setPicId("EMP9999");

        // 2) 기존 부서
        existingDepartment = new Department();
        existingDepartment.setDepartmentId(10L);
        existingDepartment.setName("홍보부");
        existingDepartment.setResponsibility("홍보 활동");
        existingDepartment.setContact("02-0000-1111");

        Pic pic = DepartmentMapper.toPic(existingDepartment, picEmployee);

        existingDepartment.setPic(pic);

        // 3) 상위 부서(가정)
        managedDepartment = new Department();
        managedDepartment.setDepartmentId(2L);
        managedDepartment.setName("본사");

        // 4) 담당자(Employee)
        picEmployee2 = new Employee();
        picEmployee2.setEmpId("EMP9999");
        picEmployee2.setName("김사원");
    }

    @Test
    @DisplayName("createDepartment - 정상 케이스: 상위부서/직원 존재 & 중복PIC 아님")
    void testCreateDepartment_success() {
        // given
        // departmentCommandRepository.findById(1L) -> Optional.of(managedDepartment)
        given(departmentCommandRepository.findById(createDTO.getManageDeptId()))
                .willReturn(Optional.of(managedDepartment));

        // employeeCommandRepository.findById("EMP001") -> Optional.of(picEmployee)
        given(employeeCommandRepository.findById(createDTO.getPicId()))
                .willReturn(Optional.of(picEmployee));

        // picCommandRepository.existsByDepartment(...) -> false
        given(picCommandRepository.existsByDepartment(any(Department.class)))
                .willReturn(false);

        // when
        departmentCommandService.createDepartment(createDTO);

        // then
        // 1) 상위 부서 조회
        then(departmentCommandRepository).should(times(1))
                .findById(1L);
        // 2) 담당자 조회
        then(employeeCommandRepository).should(times(1))
                .findById("EMP001");
        // 3) 중복PIC 검사
        then(picCommandRepository).should(times(1))
                .existsByDepartment(any(Department.class));
        // 4) 부서 저장
        then(departmentCommandRepository).should(times(1))
                .save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment - 상위부서가 없으면 예외 발생")
    void testCreateDepartment_notFoundManagedDepartment() {
        // given
        given(departmentCommandRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.createDepartment(createDTO));
        // 예외코드 확인(옵션)
        // assertEquals(ErrorCode.NOT_FOUND_DEPARTMENT, ex.getErrorCode());

        // 아래는 호출되지 않아야 함
        then(employeeCommandRepository).shouldHaveNoInteractions();
        then(picCommandRepository).shouldHaveNoInteractions();
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment - 담당자(사원) 없으면 예외 발생")
    void testCreateDepartment_notFoundPicEmployee() {
        // given
        // 상위 부서는 찾았음
        given(departmentCommandRepository.findById(1L))
                .willReturn(Optional.of(managedDepartment));
        // 담당자(사원) 못 찾음
        given(employeeCommandRepository.findById("EMP001"))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.createDepartment(createDTO));
        // assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, ex.getErrorCode());

        then(picCommandRepository).shouldHaveNoInteractions();
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment - 중복 PIC이면 예외 발생")
    void testCreateDepartment_alreadyCreatePic() {
        // given
        given(departmentCommandRepository.findById(1L))
                .willReturn(Optional.of(managedDepartment));
        given(employeeCommandRepository.findById("EMP001"))
                .willReturn(Optional.of(picEmployee));

        // 중복 부서 담당자
        given(picCommandRepository.existsByDepartment(any(Department.class)))
                .willReturn(true);

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.createDepartment(createDTO));
        // assertEquals(ErrorCode.ALREADY_CREATE_PIC, ex.getErrorCode());

        // save(...)는 호출되지 않아야 함
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }

    @Test
    @DisplayName("updateDepartment - 정상적으로 부서가 업데이트되는 경우")
    void testUpdateDepartment_success() {
        // given
        // deptId=10L에 해당하는 부서를 찾음
        given(departmentCommandRepository.findById(10L))
                .willReturn(Optional.of(existingDepartment));
        // 상위 부서: deptId=2L
        given(departmentCommandRepository.findById(2L))
                .willReturn(Optional.of(managedDepartment));
        // 담당자: empId="EMP9999"
        given(employeeCommandRepository.findById("EMP9999"))
                .willReturn(Optional.of(picEmployee2));

        // when
        departmentCommandService.updateDepartment(updateDTO, 10L);

        // then
        // 엔티티가 updateDepartment(...) 로 업데이트되었는지 검증
        assertEquals("마케팅부", existingDepartment.getName());
        assertEquals("마케팅 기획 및 운영", existingDepartment.getResponsibility());
        assertEquals("02-1234-5678", existingDepartment.getContact());
        assertNotNull(existingDepartment.getManageDept());
        assertEquals(2L, existingDepartment.getManageDept().getDepartmentId());

        // save(...) 호출 검증
        then(departmentCommandRepository).should(times(1))
                .save(existingDepartment);
    }

    @Test
    @DisplayName("updateDepartment - deptId로 부서를 찾지 못하면 예외 발생")
    void testUpdateDepartment_notFoundDepartment() {
        // given
        given(departmentCommandRepository.findById(10L))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.updateDepartment(updateDTO, 10L));

        // 예외 코드 확인(옵션)
        // assertEquals(ErrorCode.NOT_FOUND_DEPARTMENT, ex.getErrorCode());

        // 추가 조회나 save가 일어나지 않음
        then(departmentCommandRepository).should(times(0))
                .findById(2L);
        then(employeeCommandRepository).shouldHaveNoInteractions();
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }

    @Test
    @DisplayName("updateDepartment - managedDepartment(상위 부서)를 찾지 못하면 예외 발생")
    void testUpdateDepartment_notFoundManagedDepartment() {
        // given
        // 1) deptId=10L (기존 부서)는 찾았으나
        given(departmentCommandRepository.findById(10L))
                .willReturn(Optional.of(existingDepartment));
        // 2) 상위 부서는 못 찾음
        given(departmentCommandRepository.findById(2L))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.updateDepartment(updateDTO, 10L));
        // assertEquals(ErrorCode.NOT_FOUND_DEPARTMENT, ex.getErrorCode());

        // 담당자 조회, save(...)는 일어나지 않아야 함
        then(employeeCommandRepository).shouldHaveNoInteractions();
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }

    @Test
    @DisplayName("updateDepartment - picId로 담당자(Employee)를 찾지 못하면 예외 발생")
    void testUpdateDepartment_notFoundEmployee() {
        // given
        // 기존 부서, 상위 부서 조회는 성공
        given(departmentCommandRepository.findById(10L))
                .willReturn(Optional.of(existingDepartment));
        given(departmentCommandRepository.findById(2L))
                .willReturn(Optional.of(managedDepartment));

        // 직원은 못 찾음
        given(employeeCommandRepository.findById("EMP9999"))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> departmentCommandService.updateDepartment(updateDTO, 10L));
        // assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, ex.getErrorCode());

        // save(...)는 호출되지 않음
        then(departmentCommandRepository).should(never()).save(any(Department.class));
    }
}