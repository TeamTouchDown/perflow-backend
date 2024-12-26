package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeApprovalRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelCommandForTeamLeaderRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeApprovalRequestCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeApprovalRequestCommandService {

    private final WorkAttitudeApprovalRequestCommandRepository workAttitudeApprovalRequestCommandRepository;
    private final EmployeeCommandRepository employeeRepository;

    // -------------------------- 연차 API --------------------------
    // 연차 신청시 입사년도에 따라서 맞춰서 생긴 연차 갯수를 넘지 않게 체크하면서 신청할 수 있게 처리
    // 가능하다면 휴가와 연차가 날짜가 겹치는걸 피할 수 있는 서비스 로직 만들기

    public void registerAnnual(WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void updateAnnual(Long annualId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void softDeleteAnnual(Long annualId) {
    }

    public void approveAnnual(Long annualId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void rejectAnnual(Long annualId, String rejectReason) {
    }

    // -------------------------- 초과근무 API --------------------------
    
    public void createOvertime(WorkAttitudeApprovalRequestDTO requestDTO) {
    }


    public void updateOvertime(Long overtimeId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void deleteOvertime(Long overtimeId) {
    }

    public void approveOvertime(Long overtimeId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void rejectOvertime(Long overtimeId, String rejectReason) {
    }
    
    // -------------------------- 휴가 API --------------------------
    public void registerVacation(WorkAttitudeApprovalRequestDTO requestDTO) {
    }
    
    public void updateVacation(Long vacationId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void deleteVacation(Long vacationId) {
    }

    public void approveVacation(Long vacationId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void rejectVacation(Long vacationId, String rejectReason) {
    }

    // -------------------------- 출장 API --------------------------

    public void requestTravel(WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void updateTravel(Long travelId, WorkAttitudeApprovalRequestDTO requestDTO) {
    }

    public void deleteTravel(Long travelId) {
    }

    public void updateTravelStatus(Long travelId, WorkAttitudeTravelCommandForTeamLeaderRequestDTO requestDTO) {
    }
}
