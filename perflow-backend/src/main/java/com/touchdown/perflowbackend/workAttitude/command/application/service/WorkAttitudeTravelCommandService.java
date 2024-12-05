package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
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
    private final EmployeeCommandRepository employeeCommandRepository; //string 값
    private final ApproveCommandRepository approveCommandRepository;
    private final WorkAttributeTravelQueryRepository workAttributeTravelQueryRepository;

    @Transactional
    public void createTravel(WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        // empId로 Employee 객체 조회
        Employee employee = employeeCommandRepository.findById(workAttitudeTravelRequestDTO.getEmpId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
        // approveSbjId로 ApproveSbj 객체 조회
        ApproveSbj approveSbj = approveCommandRepository.findById(workAttitudeTravelRequestDTO.getApproveSbjId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
        // DTO → 엔터티 변환
        Travel travel = WorkAttitudeTravelMapper.toEntity(workAttitudeTravelRequestDTO, employee, approveSbj);
        // Travel 엔터티 저장
        workAttitudeTravelCommandRepository.save(travel);
    }
/*
    public WorkAttitudeTravelRequestDTO getTravelById(Long travelId) {

        return null;
    }*/

    @Transactional
    public void updateTravel(Long travelId, WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        Travel travel = workAttributeTravelQueryRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        travel.updateTravel(workAttitudeTravelRequestDTO);
        workAttributeTravelQueryRepository.save(travel);
    }

    @Transactional
    public void deleteTravel(Long travelId) {
        workAttitudeTravelCommandRepository.deleteById(travelId);

    }
}
