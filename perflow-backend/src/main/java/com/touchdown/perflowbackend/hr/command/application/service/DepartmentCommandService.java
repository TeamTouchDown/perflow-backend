package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.mapper.DepartmentMapper;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Pic;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PicCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentCommandService {

    private final DepartmentCommandRepository departmentCommandRepository;
    private final PicCommandRepository picCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;

    @Transactional
    public void createDepartment(DepartmentCreateDTO departmentCreateDTO) {

        Department managedDepartment = getManagedDepartment(departmentCreateDTO.getManageDeptId());

        // 부서 담당자 id로 부서 담당자 객체 생성
        Employee picEmployee = getPicEmployee(departmentCreateDTO.getPicId());

        Department newDepartment = DepartmentMapper.toEntity(departmentCreateDTO, managedDepartment);

        // 이미 존재하는 부서/부서 담당자인지 확인
        if(picCommandRepository.existsByDepartment(newDepartment)) {
            throw new CustomException(ErrorCode.ALREADY_CREATE_PIC);
        }

        // 생성된 부서, 부서 담당자로 pic(부서 담당자) 객체 생성
        Pic pic = DepartmentMapper.toPic(newDepartment, picEmployee);

        newDepartment.addPic(pic);

        departmentCommandRepository.save(newDepartment);
    }

    private Employee getPicEmployee(String picId) {

        return employeeCommandRepository.findById(picId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
    }

    private Department getManagedDepartment(Long manageDeptId) {

        return departmentCommandRepository.findById(manageDeptId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MANAGED_DEPARTMENT)
        );
    }

}
