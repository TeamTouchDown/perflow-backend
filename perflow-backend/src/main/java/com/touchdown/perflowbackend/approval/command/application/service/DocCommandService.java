package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
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
    private final DepartmentCommandRepository departmentCommandRepository;

    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        Employee createUser = findEmployeeById(createUserId);

        Template template = findTemplateById(request.getTemplateId());

        // 문서 생성
        Doc doc = DocMapper.toEntity(request, createUser, template);

        // 결재선 생성
        for (ApproveLineDTO lineDTO : request.getApproveLines()) {
            ApproveLine approveLine = createApproveLine(lineDTO, doc, createUser);
            doc.getApproveLines().add(approveLine);
        }

        docCommandRepository.save(doc);
    }

    private ApproveLine createApproveLine(ApproveLineDTO lineDTO, Doc doc, Employee createUser) {

        // 결재선 생성
        ApproveLine approveLine = ApproveLine.builder()
                .doc(doc)
                .createUser(createUser)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .build();

        // 결재 주체 생성 : 사원
        for (String empId : lineDTO.getEmployees()) {
            Employee employee = findEmployeeById(empId);
            ApproveSbj approveSbj = ApproveSbj.builder()
                    .approveLine(approveLine)
                    .sbjType(SbjType.Employee)
                    .sbjUser(employee)
                    .isPll(isParallel(lineDTO))
                    .dept(null)
                    .build();

            approveLine.getApproveSubjects().add(approveSbj);
        }

        // 결재 주체 생성 : 부서
        for (Long deptId : lineDTO.getDepartments()) {
            Department department = findDepartmentByID(deptId);
            ApproveSbj approveSbj = ApproveSbj.builder()
                    .approveLine(approveLine)
                    .sbjType(SbjType.Department)
                    .sbjUser(null)
                    .isPll(isParallel(lineDTO))
                    .dept(department)
                    .build();
            approveLine.getApproveSubjects().add(approveSbj);
        }

        return approveLine;
    }

    // 결재방식이 병렬/병렬합의인지 확인
    private Boolean isParallel(ApproveLineDTO lineDTO) {

        return lineDTO.getApproveType() == ApproveType.PLL || lineDTO.getApproveType() == ApproveType.PLLAGR;
    }

    private Template findTemplateById(Long templateId) {

        return templateCommandRepository.findById(templateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE));
    }

    private Employee findEmployeeById(String createUserId) {

        return employeeCommandRepository.findById(createUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    private Department findDepartmentByID(Long deptId) {

        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }



}
