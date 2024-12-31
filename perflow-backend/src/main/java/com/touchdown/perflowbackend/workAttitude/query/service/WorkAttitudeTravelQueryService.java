package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeTravelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkAttitudeTravelQueryService {

    private final WorkAttitudeTravelQueryRepository workAttitudeTravelQueryRepository;

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getTravelsForEmployee() {
        String currentEmpId = EmployeeUtil.getEmpId();
        List<Travel> travels = workAttitudeTravelQueryRepository
                .findByEmployee_EmpIdAndStatusNot(currentEmpId, Status.DELETED);
        return travels.stream()
                .map(WorkAttitudeTravelMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getAllTravelsForLeader() {
        String currentApproverId = EmployeeUtil.getEmpId();
        List<Travel> travels = workAttitudeTravelQueryRepository
                .findByApprover_EmpIdAndStatusNot(currentApproverId, Status.DELETED);
        return travels.stream()
                .map(WorkAttitudeTravelMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getPendingTravelsForLeader() {
        String currentApproverId = EmployeeUtil.getEmpId();
        List<Travel> travels = workAttitudeTravelQueryRepository
                .findByApprover_EmpIdAndTravelStatusAndStatusNot(currentApproverId, Status.PENDING, Status.DELETED);
        return travels.stream()
                .map(WorkAttitudeTravelMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getAllTravelsForHR() {
        List<Travel> travels = workAttitudeTravelQueryRepository
                .findByStatusNot(Status.DELETED);
        return travels.stream()
                .map(WorkAttitudeTravelMapper::toResponseDTO)
                .toList();
    }
}
