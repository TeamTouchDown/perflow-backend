package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttributeTravelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkAttitudeTravelQueryService {

    private final WorkAttributeTravelQueryRepository workAttributeTravelQueryRepository;

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getTravelsForEmployee(){
        String empId = EmployeeUtil.getEmpId();
        List<Travel> travels = workAttributeTravelQueryRepository.findAllByEmployeeAndNotDeleted(empId);
        return travels.stream()
                .map(WorkAttitudeTravelMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<WorkAttitudeTravelResponseDTO> getAllTravelsForLeader() {

        List<Travel> travels = workAttributeTravelQueryRepository.findAll();
                return travels.stream()
                        .map(WorkAttitudeTravelMapper::toResponseDTO)
                        .toList();
    }
}// 조회도 deleted 된게 아닌 조회만 될 수 있게 직원은
// 팀장은 삭제된 조회도 같이 볼 수 있게 만들어ㅑ됨
