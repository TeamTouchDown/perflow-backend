package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveLineCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final TemplateCommandRepository templateCommandRepository;
    private final DocCommandRepository docCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final ApproveLineCommandRepository approveLineCommandRepository;

    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        Employee createUser = findEmployeeById(createUserId);

        Template template = findTemplateById(request.getTemplateId());

        // 문서 생성
        Doc doc = createDoc(request, createUser, template);

        // 결재선 생성
        createApproveLines(request, doc, createUser);

        // 공유 설정
        createShare(request, doc, createUser);

        docCommandRepository.save(doc);
    }

    private void createShare(DocCreateRequestDTO request, Doc doc, Employee createUser) {

        for (ShareDTO shareDTO : request.getShares()) {

            // 공유 대상이 사원인 경우
            for (String empId : shareDTO.getEmployees()) {
                Employee shareUser = findEmployeeById(empId);
                DocShareObj shareObj = createShareObj(doc, ObjType.EMPLOYEE, shareUser, null, createUser);
                doc.getShares().add(shareObj);
            }

            // 공유 대상이 부서인 경우
            for(Long deptId : shareDTO.getDepartments()) {
                Department shareDepartment = findDepartmentByID(deptId);
                DocShareObj shareObj = createShareObj(doc, ObjType.DEPARTMENT, null, shareDepartment, createUser);
                doc.getShares().add(shareObj);
            }
        }
    }

    private DocShareObj createShareObj(Doc doc, ObjType objType, Employee shareUser, Department shareDepartment, Employee createUser) {

        return DocShareObj.builder()
                .doc(doc)
                .shareObjType(objType)
                .shareObjUser(shareUser)
                .shareObjDepartment(shareDepartment)
                .shareAddUser(createUser)
                .build();
    }

    // 결재선 리스트 추가
    private void createApproveLines(DocCreateRequestDTO request, Doc doc, Employee createUser) {

        for (ApproveLineDTO lineDTO : request.getApproveLines()) {
            ApproveLine approveLine;

            if (lineDTO.getApproveTemplateTypes() == MY_APPROVE_LINE) {
                // 나의 결재선에서 불러온 경우
                approveLine = loadMyApproveLine(lineDTO, doc, createUser);
            } else if (lineDTO.getApproveTemplateTypes() == MANUAL) {
                // 직접 생성한 경우
                approveLine = createApproveLine(lineDTO, doc, createUser);
            } else {
                throw new CustomException(ErrorCode.INVALID_APPROVE_TEMPLATE_TYPE);
            }
            doc.getApproveLines().add(approveLine);
        }
    }

    // todo: 나의 결재선 불러오기 수정 필요!!
    private ApproveLine loadMyApproveLine(ApproveLineDTO lineDTO, Doc doc, Employee createUser) {

        return ApproveLine.builder()
                .doc(doc)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .createUser(createUser)
                .build();
    }

    // 문서 객체 생성
    private Doc createDoc(DocCreateRequestDTO request, Employee createUser, Template template) {

        return DocMapper.toEntity(request, createUser, template);
    }

    // 결재선 추가
    private ApproveLine createApproveLine(ApproveLineDTO lineDTO, Doc doc, Employee createUser) {

        ApproveLine approveLine = ApproveLine.builder()
                .doc(doc)
                .createUser(createUser)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .build();

        addApproveSbjs(lineDTO, approveLine);

        return approveLine;
    }

    // 결재 주체 추가
    private void addApproveSbjs(ApproveLineDTO lineDTO, ApproveLine approveLine) {

        // 결재 주체가 사원인 경우
        for (String empId : lineDTO.getEmployees()) {
            Employee employee = findEmployeeById(empId);
            ApproveSbj approveSbj = createApproveSbj(approveLine, SbjType.EMPLOYEE, employee, null, isParallel(lineDTO));
            approveLine.getApproveSubjects().add(approveSbj);
        }

        // 결재 주체가 부서인 경우
        for (Long deptId : lineDTO.getDepartments()) {
            Department department = findDepartmentByID(deptId);
            ApproveSbj approveSbj = createApproveSbj(approveLine, SbjType.DEPARTMENT, null, department, isParallel(lineDTO));
            approveLine.getApproveSubjects().add(approveSbj);
        }
    }

    private ApproveSbj createApproveSbj(ApproveLine approveLine, SbjType sbjType, Employee sbjUser, Department dept, boolean isParallel) {

        return ApproveSbj.builder()
                .approveLine(approveLine)
                .sbjType(sbjType)
                .sbjUser(sbjUser)
                .dept(dept)
                .isPll(isParallel)
                .build();
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
