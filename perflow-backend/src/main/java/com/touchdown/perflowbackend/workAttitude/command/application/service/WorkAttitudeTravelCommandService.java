package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeTravelCommandService {

    private final WorkAttitudeTravelCommandRepository travelRepository;
    private final EmployeeCommandRepository employeeRepository;

    private Employee getCurrentEmployee() {
        String currentEmpId = EmployeeUtil.getEmpId();
        return employeeRepository.findById(currentEmpId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    private void validateDateOverlap(String empId, LocalDateTime start, LocalDateTime end) {
        boolean overlap = travelRepository.existsByEmployee_EmpIdAndStatusNotAndTravelEndGreaterThanEqualAndTravelStartLessThanEqual(
                empId, Status.DELETED, start, end
        );
        if (overlap) {
            throw new CustomException(ErrorCode.DUPLICATE_TRAVEL);
        }
    }

    @Transactional
    public void requestTravel(WorkAttitudeTravelRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
        validateDateOverlap(employee.getEmpId(), requestDTO.getTravelStart(), requestDTO.getTravelEnd());
        Travel travel = WorkAttitudeTravelMapper.toEntity(requestDTO, employee, approver);
        travelRepository.save(travel);
        log.info("출장 신청 완료: {}", travel);
    }

    @Transactional
    public void updateTravel(Long travelId, WorkAttitudeTravelRequestDTO requestDTO) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        if (!travel.getEmployee().getEmpId().equals(getCurrentEmployee().getEmpId())) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }
        travel.updateTravel(
                requestDTO.getTravelReason(),
                requestDTO.getTravelStart(),
                requestDTO.getTravelEnd(),
                requestDTO.getTravelDivision()
        );
        travelRepository.save(travel);
        log.info("출장 수정 완료: {}", travel);
    }

    @Transactional
    public void deleteTravel(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        if (!travel.getEmployee().getEmpId().equals(getCurrentEmployee().getEmpId())) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }
        travel.deleteTravel();
        travelRepository.save(travel);
        log.info("출장 삭제(소프트 딜리트) 완료: {}", travel);
    }

    @Transactional
    public void approveTravel(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        travel.updateTravelStatus(Status.CONFIRMED, null);
        travelRepository.save(travel);
        log.info("출장 승인 완료: {}", travel);
    }

    @Transactional
    public void rejectTravel(Long travelId, String rejectReason) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        travel.updateTravelStatus(Status.REJECTED, rejectReason);
        travelRepository.save(travel);
        log.info("출장 반려 완료: {}", travel);
    }
}
