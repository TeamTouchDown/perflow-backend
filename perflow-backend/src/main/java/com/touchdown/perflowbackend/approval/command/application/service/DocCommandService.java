package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final TemplateCommandRepository templateCommandRepository;
    private final DocCommandRepository docCommandRepository;

    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        Employee createUser = findEmployeeById(createUserId);

        Template template = findTemplateById(request.getTemplateId());

        // 문서 생성
        Doc doc = DocMapper.toEntity(request, createUser, template);

        // 결재선 생성
        for (ApproveLineDTO lineDTO : request.getApproveLines()) {
            ApproveLine approveLine = createApproveLine(lineDTO, doc);
            doc.getApproveLines().add(approveLine);
        }

        docCommandRepository.save(doc);
    }

    private ApproveLine createApproveLine(ApproveLineDTO lineDTO, Doc doc) {

        // 결재선 생성
        ApproveLine approveLine = ApproveLine.builder()
                .doc(doc)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .build();

        // 결재 주체 생성
        for (String empId : lineDTO.getEmployees()) {
            Employee employee = findEmployeeById(empId);
            ApproveSbj approveSbj = ApproveSbj.builder()
                    .approveLine(approveLine)
                    .sbjUser(employee)
                    .build();

            approveLine.getApproveSubjects().add(approveSbj);
        }

        return approveLine;
    }

    private Template findTemplateById(Long templateId) {

        return templateCommandRepository.findById(templateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE));
    }

    private Employee findEmployeeById(String createUserId) {

        return employeeCommandRepository.findById(createUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }



}
