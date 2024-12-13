package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Type;
import com.touchdown.perflowbackend.hr.command.domain.repository.AppointCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointCommandService {

    private final AppointCommandRepository appointCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;

    public void createAppoint(AppointCreateDTO appointCreateDTO) {

        Employee employee = getEmployee(appointCreateDTO.getEmpId());

        switch (appointCreateDTO.getType()) {
            case PROMOTION -> {
                promotionEmployee(employee, appointCreateDTO);
                break;
            }
            case DEMOTION -> {
                demotionEmployee(employee, appointCreateDTO);
                break;
            }
            case TRANSFER -> {
                transferEmployee(employee, appointCreateDTO);
            }
        }

    }

    private Employee getEmployee(String id) {

        return employeeCommandRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
    }

    private Position getAfterPosition(Long positionId) {

        return positionCommandRepository.findById(positionId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POSITION)
        );
    }

    private Department getAfterDepartment(Long departmentId) {

        return departmentCommandRepository.findById(departmentId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT)
        );
    }

    private void promotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(appointCreateDTO.getAfter());

        // 승진은 한번에 한 단계씩 이루어져야 함.
        if(after.getPositionLevel() - before.getPositionLevel() != 1) {
            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);
    }

    private void demotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(appointCreateDTO.getAfter());

        // 강등은 한번에 한 단계씩 이루어져야 함.
        if(after.getPositionLevel() - before.getPositionLevel() != -1) {

            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);
    }

    private void transferEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Department before = employee.getDept();
        Department after = getAfterDepartment(appointCreateDTO.getAfter());

        // 이미 소속된 부서로 이동을 신청했을 때.
        if(before.equals(after)) {

            throw new CustomException(ErrorCode.ALREADY_IN_DEPARTMENT);
        }

        // 부서 업데이트
        employee.updateDepartment(after);
    }

}
