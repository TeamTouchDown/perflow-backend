package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkAttitudeTravelCommandService {

    private final WorkAttitudeTravelCommandRepository workAttitudeTravelCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository; //string 값

    public void createTravel(WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
        // empId로 Employee 객체 조회
        Employee employee = employeeCommandRepository.findById(workAttitudeTravelRequestDTO.getEmpId())
                .orElseThrow(() -> {
                    return new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE);
                });

        // DTO → 엔터티 변환
        Travel travel = WorkAttitudeTravelMapper.toEntity(workAttitudeTravelRequestDTO, employee);

        // Travel 엔터티 저장
        workAttitudeTravelCommandRepository.save(travel);
    }

    public WorkAttitudeTravelRequestDTO getTravelById(Long travelId) {
        return null;
    }

    public void updateTravel(Long travelId, WorkAttitudeTravelRequestDTO workAttitudeTravelRequestDTO) {
    }

    public void deleteTravel(Long travelId) {

    }
}
