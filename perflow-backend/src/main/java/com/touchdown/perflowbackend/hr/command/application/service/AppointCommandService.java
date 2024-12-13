package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.AppointMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.*;
import com.touchdown.perflowbackend.hr.command.domain.repository.AppointCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointCommandService {

    private final AppointCommandRepository appointCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final JobCommandRepository jobCommandRepository;

    @Transactional
    public void createAppoint(AppointCreateDTO appointCreateDTO) {

        Employee employee = getEmployee(appointCreateDTO.getEmpId());

        String after = null;
        String before = null;

        switch (appointCreateDTO.getType()) {

            case PROMOTION -> { // 승진

                after = promotionEmployee(employee, appointCreateDTO);
                before = employee.getPosition().getName();
                break;
            }
            case DEMOTION -> { // 강등

                after = demotionEmployee(employee, appointCreateDTO);
                before = employee.getPosition().getName();
                break;
            }
            case TRANSFER -> { // 부서이동

                after = transferEmployee(employee, appointCreateDTO);
                before = employee.getDept().getName();
                break;
            }
            case CHANGE_JOB -> {

                after = changeJobEmployee(employee, appointCreateDTO);
                before = employee.getJob().getName();
                break;
            }
        }

        Appoint appoint = AppointMapper.toEntity(appointCreateDTO, employee, after, before);

        appointCommandRepository.save(appoint);
    }

    private String promotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(appointCreateDTO.getAfter());

        // 승진은 한번에 한 단계씩 이루어져야 함.
        if (after.getPositionLevel() - before.getPositionLevel() != 1) {
            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);

        return after.getName();
    }

    private String demotionEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Position before = employee.getPosition();
        Position after = getAfterPosition(appointCreateDTO.getAfter());

        // 강등은 한번에 한 단계씩 이루어져야 함.
        if (after.getPositionLevel() - before.getPositionLevel() != -1) {

            throw new CustomException(ErrorCode.TOO_MANY_PROMOTION_STEPS);
        }

        // 직위 업데이트
        employee.updatePosition(after);

        return after.getName();
    }

    private String transferEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Department before = employee.getDept();
        Department after = getAfterDepartment(appointCreateDTO.getAfter());

        // 이미 소속된 부서로 이동을 신청했을 때.
        if (before.equals(after)) {

            throw new CustomException(ErrorCode.DUPLICATE_DEPT_REQUEST);
        }

        // 부서 업데이트
        employee.updateDepartment(after);

        return after.getName();
    }

    private String changeJobEmployee(Employee employee, AppointCreateDTO appointCreateDTO) {

        Job before = employee.getJob();
        Job after = getAfterJob(appointCreateDTO.getAfter());

        if (before.equals(after)) {

            throw new CustomException(ErrorCode.DUPLICATE_JOB_REQUEST);
        }

        // 직책 업데이트
        employee.updateJob(after);

        return after.getName();
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

    private Job getAfterJob(Long jobId) {

        return jobCommandRepository.findById(jobId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_JOB)
        );
    }

}
