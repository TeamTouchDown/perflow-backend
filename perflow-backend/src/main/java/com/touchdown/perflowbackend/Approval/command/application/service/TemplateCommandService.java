package com.touchdown.perflowbackend.Approval.command.application.service;

import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.Approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.Approval.command.mapper.TemplateMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TemplateCommandService {

    private final TemplateCommandRepository templateCommandRepository;

    // 기본 정보를 받아 서식을 생성하는 메소드
    @Transactional
    public TemplateCreateResponseDTO createTemplateWithBasicInfo(TemplateCreateRequestDTO request, String empId) {

        // 로그인 한 사원 조회
        Employee createEmp = employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 서식 엔터티 생성
        Template newTemplate = TemplateMapper.templateToEntity(createEmp, request);

        // 서식 저장
        Template savedTemplate = templateCommandRepository.save(newTemplate);

        return new TemplateCreateResponseDTO(savedTemplate.getTemplateId());
    }
}
