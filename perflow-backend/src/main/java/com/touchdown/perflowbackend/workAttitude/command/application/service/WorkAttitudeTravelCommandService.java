package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelCommandForTeamLeaderRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
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
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);
        ApproveSbj approveSbj = findApproveSbjById(workAttitudeTravelRequestDTO.getApproveSbjId());

        Travel travel = WorkAttitudeTravelMapper.toEntity(workAttitudeTravelRequestDTO, employee, approveSbj);
        workAttitudeTravelCommandRepository.save(travel);
    }

    @Transactional
    public void updateTravel(Long travelId, WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        String empId = EmployeeUtil.getEmpId();
        Travel travel = findById(travelId);

        // 본인이 작성한 출장인지 확인
        if (!travel.getEmployee().getEmpId().equals(empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER); // 작성자 불일치 에러
        }

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
        String empId = EmployeeUtil.getEmpId();
        Travel travel = findById(travelId);

        // 본인이 작성한 출장인지 확인
        if (!travel.getEmployee().getEmpId().equals(empId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER); // 작성자 불일치 에러
        }
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

    public void updateTravelStatus(Long travelId, WorkAttitudeTravelCommandForTeamLeaderRequestDTO requestDTO) {
        Travel travel = findById(travelId);

        // 승인 또는 반려 상태 업데이트
        if ("REJECTED".equals(requestDTO.getTravelStatus())) {
            travel.updateTravelStatus(Status.REJECTED, requestDTO.getRejectReason());
        } else if ("CONFIRMED".equals(requestDTO.getTravelStatus())) {
            travel.updateTravelStatus(Status.CONFIRMED, null); // 승인 시 반려 사유 초기화
        } else {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }

        workAttitudeTravelCommandRepository.save(travel);
    }
}

