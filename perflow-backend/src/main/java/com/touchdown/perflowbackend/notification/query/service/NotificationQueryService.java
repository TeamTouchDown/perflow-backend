package com.touchdown.perflowbackend.notification.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.notification.query.dto.NotificationResponseDTO;
import com.touchdown.perflowbackend.notification.query.mapper.NotificationMapper;
import com.touchdown.perflowbackend.notification.query.repository.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {

    private final NotificationQueryRepository notificationQueryRepository;
    private final EmployeeQueryRepository employeeQueryRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotifications(String empId) {

        return notificationQueryRepository.findAllByEmployee(getEmployee(empId))
                .stream()
                .map(NotificationMapper::toDTO)
                .sorted(NotificationResponseDTO.byCreateDatetimeDesc)
                .toList();
    }

    private Employee getEmployee(String empId) {
        return employeeQueryRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
}
