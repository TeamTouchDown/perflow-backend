package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttributeTravelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttitudeTravelCommandService {

    private final WorkAttitudeTravelCommandRepository workAttitudeTravelCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository; // string 값
    private final ApproveCommandRepository approveCommandRepository;


    @Transactional
    public void createTravel(WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        Employee employee = findEmployeeByEmpId(workAttitudeTravelRequestDTO.getEmpId());
        ApproveSbj approveSbj = findApproveSbjById(workAttitudeTravelRequestDTO.getApproveSbjId());

        Travel travel = WorkAttitudeTravelMapper.toEntity(workAttitudeTravelRequestDTO, employee, approveSbj);
        workAttitudeTravelCommandRepository.save(travel);
    }

    @Transactional
    public void updateTravel(Long travelId, WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        Travel travel = findById(travelId);

        travel.updateTravel(
                workAttitudeTravelRequestDTO.getTravelReason(),
                workAttitudeTravelRequestDTO.getTravelStart(),
                workAttitudeTravelRequestDTO.getTravelEnd(),
                workAttitudeTravelRequestDTO.getTravelDivision()
                );
        workAttitudeTravelCommandRepository.save(travel);
    }

    @Transactional
    public void deleteTravel(Long travelId) {
        Travel travel = findById(travelId);
        travel.deleteTravel();
        workAttitudeTravelCommandRepository.save(travel);
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    private ApproveSbj findApproveSbjById(Long approveSbjId) {
        return approveCommandRepository.findById(approveSbjId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }

    private Travel findById(Long travelId) {
        return workAttitudeTravelCommandRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
    }

}

