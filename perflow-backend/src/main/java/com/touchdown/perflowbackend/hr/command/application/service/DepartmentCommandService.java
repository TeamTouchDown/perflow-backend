package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.Mapper.DepartmentMapper;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentCommandService {

    private final DepartmentCommandRepository departmentCommandRepository;

    @Transactional
    public void createDepartment(DepartmentCreateDTO departmentCreateDTO) {

        Department managedDepartment = getManagedDepartment(departmentCreateDTO.getManageDeptId());

        Department newDepartment = DepartmentMapper.toEntity(departmentCreateDTO, managedDepartment);

        departmentCommandRepository.save(newDepartment);
    }

    private Department getManagedDepartment(Long manageDeptId) {

        return departmentCommandRepository.findById(manageDeptId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MANAGED_DEPARTMENT)
        );
    }
}
