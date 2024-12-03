package com.touchdown.perflowbackend.Approval.command.application.service;

import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.TemplateField;
import com.touchdown.perflowbackend.Approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.Approval.command.domain.repository.TemplateFieldCommandRepository;
import com.touchdown.perflowbackend.Approval.command.mapper.TemplateFieldMapper;
import com.touchdown.perflowbackend.Approval.command.mapper.TemplateMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateCommandService {

    private final TemplateCommandRepository templateCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final TemplateFieldCommandRepository templateFieldCommandRepository;

    @Transactional
    public TemplateCreateResponseDTO createNewTemplate(TemplateCreateRequestDTO request, String empId) {

        // 사원 조회
        Employee createEmp = employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        log.info("사번: {}", createEmp.getEmpId());

        // 서식 엔터티 생성, 저장
        Template newTemplate = TemplateMapper.templateToEntity(createEmp, request);
        Template savedTemplate = templateCommandRepository.save(newTemplate);

        if (request.getFields() != null) {

            log.info("필드가 1개 이상 있음");

            List<TemplateField> fields = TemplateFieldMapper.templateFieldsToEntities(savedTemplate, request.getFields());

            log.info("templateFieldCommandRepository.save 실행");

            templateFieldCommandRepository.saveAll(fields);

            log.info("templateFieldCommandRepository.save 실행 완료");
        }

        return new TemplateCreateResponseDTO(savedTemplate.getTemplateId());
    }
}
