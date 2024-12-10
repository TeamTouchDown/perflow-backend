package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.MyApproveLineCreateRequestDTO;
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

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void createNewMyApproveLine(MyApproveLineCreateRequestDTO request, String createUserId) {

        Employee createUser = findEmployeeById(createUserId);

        Long groupId = generateGroupId();
        
        for(ApproveLineDTO lineDTO : request.getApproveLines()) {

            ApproveLine approveLine = ApproveLine.builder()
                    .doc(null)
                    .groupId(groupId)
                    .createUser(createUser)
                    .approveTemplateType(MY_APPROVE_LINE)
                    .name(request.getName())
                    .description(request.getDescription())
                    .approveType(lineDTO.getApproveType())
                    .approveLineOrder(lineDTO.getApproveLineOrder())
                    .pllGroupId(lineDTO.getPllGroupId())
                    .build();

            addApproveSbjs(lineDTO, approveLine);

            approveLineCommandRepository.save(approveLine);
        }
    }

    private Long generateGroupId() {

        return Math.abs(UUID.randomUUID().getMostSignificantBits());
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

    private ApproveLine loadMyApproveLine(ApproveLineDTO lineDTO, Doc doc, Employee createUser) {

        // 나의 결재선 불러오기
        Long groupId = lineDTO.getApproveLineId();
        List<ApproveLine> myApproveLines = approveLineCommandRepository.findByGroupId(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE));

        ApproveLine copiedApproveLine = null;

        for (ApproveLine myApproveLine : myApproveLines) {
            ApproveLine newApproveLine = ApproveLine.builder()
                    .doc(doc) // 새로운 문서에 연결
                    .groupId(doc.getDocId()) // 새로운 문서 ID로 그룹화
                    .createUser(createUser)
                    .approveTemplateType(MY_APPROVE_LINE)
                    .name(myApproveLine.getName())
                    .description(myApproveLine.getDescription())
                    .approveType(myApproveLine.getApproveType())
                    .approveLineOrder(myApproveLine.getApproveLineOrder())
                    .pllGroupId(myApproveLine.getPllGroupId())
                    .build();

            // 기존 결재 주체 복사
            for (ApproveSbj myApproveSbj : myApproveLine.getApproveSubjects()) {
                ApproveSbj newApproveSbj = ApproveSbj.builder()
                        .approveLine(newApproveLine)
                        .sbjType(myApproveSbj.getSbjType())
                        .sbjUser(myApproveSbj.getSbjUser())
                        .dept(myApproveSbj.getDept())
                        .isPll(myApproveSbj.getIsPll())
                        .build();

                newApproveLine.getApproveSubjects().add(newApproveSbj);
            }

            if (copiedApproveLine == null) {
                copiedApproveLine = newApproveLine; // 복사한 첫 번째 결재선을 반환
            }

            doc.getApproveLines().add(newApproveLine);
        }

        return copiedApproveLine; // 첫 번째 결재선 반환
    }

    // 문서 객체 생성
    private Doc createDoc(DocCreateRequestDTO request, Employee createUser, Template template) {

        return DocMapper.toEntity(request, createUser, template);
    }

    // 결재선 추가
    private ApproveLine createApproveLine(ApproveLineDTO lineDTO, Doc doc, Employee createUser) {

        ApproveLine approveLine = ApproveLine.builder()
                .doc(doc)
                .groupId(doc.getDocId())    // 문서 id 를 groupId로
                .approveTemplateType(lineDTO.getApproveTemplateTypes())
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
