package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeApprovalRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeApprovalRequestCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeApprovalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeApprovalRequestCommandService {

    private final WorkAttitudeApprovalRequestCommandRepository approvalRequestRepository;
    private final EmployeeCommandRepository employeeRepository;

    // 현재 로그인한 사용자 조회
    private Employee getCurrentEmployee() {
        String empId = EmployeeUtil.getEmpId();
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    // 날짜 중복 검증 메소드
    private boolean isDateOverlap(String empId, LocalDateTime startDate, LocalDateTime endDate, RequestType requestType) {
        return approvalRequestRepository.existsByEmpId_EmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
                empId, requestType, Status.PENDING, DeleteStatus.ACTIVATED, startDate, endDate
        );
    }
    private void validateDateOverlap(String empId, LocalDateTime startDate, LocalDateTime endDate, RequestType... requestTypes) {
        for (RequestType requestType : requestTypes) {
            if (isDateOverlap(empId, startDate, endDate, requestType)) {
                throw new CustomException(ErrorCode.DUPLICATE_ANNUAL);
            }
        }
    }

    private void validateAnnualCount(Employee employee, LocalDateTime start, LocalDateTime endDate, AnnualType annualType) {
        // 1. 제공된 연차 계산
        int providedAnnualCount = calculateProvidedAnnualCount(employee.getJoinDate());

        // 2. 이미 사용한 연차 조회
        long usedAnnualCount = approvalRequestRepository.countByEmpId_EmpIdAndRequestTypeAndStatus(
                employee.getEmpId(), RequestType.ANNUAL, Status.CONFIRMED
        );

        // 3. 신청한 연차 일수 계산
        double requestedDays = calculateDaysBetween(start, endDate, annualType);

        // 4. 검증 - 제공된 연차 초과 여부 확인
        if ((usedAnnualCount + requestedDays) > providedAnnualCount) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_ANNUAL);
        }
    }


    private int calculateProvidedAnnualCount(LocalDate joinDate) {

        int baseAnnual = 15; // 기본 15일
        int maxAnnual = 25; // 최대 25일

        // 근속연수 계산
        int years = LocalDate.now().getYear() - joinDate.getYear();

        // 3년마다 1일씩 추가
        int additionalAnnual = (years / 3);

        // 최대 25일 제한 적용
        return Math.min(baseAnnual + additionalAnnual, maxAnnual);
    }

    private long calculateDaysBetween(LocalDateTime startDate, LocalDateTime endDate, AnnualType type) {

        long days = java.time.Duration.between(startDate.toLocalDate().atStartOfDay(), endDate.toLocalDate().atStartOfDay()).toDays() + 1;

        if (type == AnnualType.MORNINGHALF || type == AnnualType.AFTERNOONHALF) {
            return (long) (days - 0.5); // 반차일 경우 0.5일 차감
        }
        return days;
    }








    // -------------------------- 연차 API --------------------------
    @Transactional
    public void registerAnnual(WorkAttitudeApprovalRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

        // 날짜 중복 검증
        validateDateOverlap(employee.getEmpId(), requestDTO.getAnnualStart(), requestDTO.getAnnualEnd(),
                RequestType.ANNUAL, RequestType.VACATION);

        // 연차 개수 검증 추가
        validateAnnualCount(employee, requestDTO.getAnnualStart(), requestDTO.getAnnualEnd(), requestDTO.getAnnualType());

        ApprovalRequest approvalRequest = WorkAttitudeApprovalMapper.toEntity(requestDTO, employee, approver);
        approvalRequestRepository.save(approvalRequest);
        log.info("연차 신청 완료: {}", approvalRequest);
    }

    @Transactional
    public void updateAnnual(Long annualId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid annual ID"));
        WorkAttitudeApprovalMapper.updateEntityFromDto(requestDTO, approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("연차 수정 완료: {}", approvalRequest);
    }

    @Transactional
    public void softDeleteAnnual(Long annualId) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid annual ID"));
        WorkAttitudeApprovalMapper.softDelete(approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("연차 삭제 완료: {}", approvalRequest);
    }

    @Transactional
    public void approveAnnual(Long annualId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid annual ID"));
        approvalRequest.setStatus(Status.CONFIRMED);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("연차 승인 완료: {}", approvalRequest);
    }

/*    @Transactional
    public void rejectAnnual(Long annualId, String rejectReason) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(annualId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid annual ID"));
        approvalRequest.setStatus(Status.REJECTED);
        approvalRequest.setRejectReason(rejectReason);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("연차 반려 완료: {}", approvalRequest);
    }*/

    // -------------------------- 초과근무 API --------------------------
    @Transactional
    public void createOvertime(WorkAttitudeApprovalRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid approver ID"));
        ApprovalRequest approvalRequest = WorkAttitudeApprovalMapper.toEntity(requestDTO, employee, approver);
        approvalRequestRepository.save(approvalRequest);
        log.info("초과근무 신청 완료: {}", approvalRequest);
    }

    @Transactional
    public void updateOvertime(Long overtimeId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(overtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid overtime ID"));
        WorkAttitudeApprovalMapper.updateEntityFromDto(requestDTO, approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("초과근무 수정 완료: {}", approvalRequest);
    }

    @Transactional
    public void deleteOvertime(Long overtimeId) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(overtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid overtime ID"));
        WorkAttitudeApprovalMapper.softDelete(approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("초과근무 삭제 완료: {}", approvalRequest);
    }

    @Transactional
    public void approveOvertime(Long overtimeId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(overtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid overtime ID"));
        approvalRequest.setStatus(Status.CONFIRMED);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("초과근무 승인 완료: {}", approvalRequest);
    }

    @Transactional
    public void rejectOvertime(Long overtimeId, String rejectReason) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(overtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid overtime ID"));
        approvalRequest.setStatus(Status.REJECTED);
        approvalRequest.setRejectReason(rejectReason);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("초과근무 반려 완료: {}", approvalRequest);
    }

    // -------------------------- 휴가 API --------------------------
    @Transactional
    public void registerVacation(WorkAttitudeApprovalRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid approver ID"));


        // 날짜 중복 체크 (연차, 휴가)
        validateDateOverlap(employee.getEmpId(), requestDTO.getAnnualStart(), requestDTO.getAnnualEnd(),
                RequestType.ANNUAL, RequestType.VACATION);

        ApprovalRequest approvalRequest = WorkAttitudeApprovalMapper.toEntity(requestDTO, employee, approver);
        approvalRequestRepository.save(approvalRequest);
        log.info("휴가 신청 완료: {}", approvalRequest);
    }

    @Transactional
    public void updateVacation(Long vacationId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vacation ID"));
        WorkAttitudeApprovalMapper.updateEntityFromDto(requestDTO, approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("휴가 수정 완료: {}", approvalRequest);
    }

    @Transactional
    public void deleteVacation(Long vacationId) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vacation ID"));
        WorkAttitudeApprovalMapper.softDelete(approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("휴가 삭제 완료: {}", approvalRequest);
    }

    @Transactional
    public void approveVacation(Long vacationId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vacation ID"));
        approvalRequest.setStatus(Status.CONFIRMED);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("휴가 승인 완료: {}", approvalRequest);
    }

    @Transactional
    public void rejectVacation(Long vacationId, String rejectReason) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vacation ID"));
        approvalRequest.setStatus(Status.REJECTED);
        approvalRequest.setRejectReason(rejectReason);
        approvalRequest.setUpdateDatetime(LocalDateTime.now());
        approvalRequestRepository.save(approvalRequest);
        log.info("휴가 반려 완료: {}", approvalRequest);
    }

    // -------------------------- 출장 API --------------------------
    @Transactional
    public void requestTravel(WorkAttitudeApprovalRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_EMPLOYEE.getMessage()));

        ApprovalRequest approvalRequest = WorkAttitudeApprovalMapper.toEntity(requestDTO, employee, approver);
        approvalRequestRepository.save(approvalRequest);
        log.info("출장 신청 완료: {}", approvalRequest);
    }

    @Transactional
    public void updateTravel(Long travelId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(travelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAVEL.getMessage()));
        WorkAttitudeApprovalMapper.updateEntityFromDto(requestDTO, approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("출장 수정 완료: {}", approvalRequest);
    }

    @Transactional
    public void deleteTravel(Long travelId) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(travelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAVEL.getMessage()));
        WorkAttitudeApprovalMapper.softDelete(approvalRequest);
        approvalRequestRepository.save(approvalRequest);
        log.info("출장 삭제 완료: {}", approvalRequest);
    }

    @Transactional
    public void updateTravelStatus(Long travelId, WorkAttitudeApprovalRequestDTO requestDTO) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findById(travelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAVEL.getMessage()));

        approvalRequest.setStatus(requestDTO.getStatus());
        approvalRequest.setRejectReason(requestDTO.getRejectReason());
        approvalRequest.setUpdateDatetime(LocalDateTime.now());

        approvalRequestRepository.save(approvalRequest);
        log.info(" 결재, 반려 변경 완료: {}", approvalRequest);
    }
}
