package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Type;
import com.touchdown.perflowbackend.hr.command.domain.repository.AppointCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointCommandService {

    private final AppointCommandRepository appointCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;

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

    private void promotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(Long.parseLong(appointCreateDTO.getAfter()));

        // 승진은 한번에 한 단계씩 이루어져야 함.
        if(after.getPositionLevel() - before.getPositionLevel() != 1) {
            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);
    }

    private void demotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(Long.parseLong(appointCreateDTO.getAfter()));

        // 강등은 한번에 한 단계씩 이루어져야 함.
        if(after.getPositionLevel() - before.getPositionLevel() != -1) {
            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);
    }

}
