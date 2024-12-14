package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.*;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.ApproveSbjDTO;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType.MY_APPROVE_LINE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final TemplateCommandRepository templateCommandRepository;
    private final DocCommandRepository docCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final ApproveLineCommandRepository approveLineCommandRepository;
    private final DocFieldCommandRepository docFieldCommandRepository;
    private final DocShareObjCommandRepository docShareObjCommandRepository;
    private final TemplateFieldCommandRepository templateFieldCommandRepository;


    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        log.info("createNewDoc 실행");
        // 사원 조회
        Employee createUser = findEmployeeById(createUserId);

        // 서식 조회
        Template template = findTemplateById(request.getTemplateId());

        // 문서 저장
        Doc doc = createDoc(request, createUser, template);
        docCommandRepository.save(doc);

        // 필드 데이터 설정
        createDocFields(request, doc);

        // 결재선 생성
        createApproveLines(request, doc, createUser);

        // 공유 설정
        createShare(request, doc, createUser);
    }

    private void createShare(DocCreateRequestDTO request, Doc doc, Employee createUser) {

        // 모든 empId와 departmentId 추출
        Set<String> empIds = request.getShares().stream()
                .filter(shareDTO -> shareDTO.getShareEmpDeptType() == EmpDeptType.EMPLOYEE)
                .flatMap(shareDTO -> shareDTO.getEmployees().stream())
                .collect(Collectors.toSet());

        Set<Long> departmentIds = request.getShares().stream()
                .filter(shareDTO -> shareDTO.getShareEmpDeptType() == EmpDeptType.DEPARTMENT)
                .flatMap(shareDTO -> shareDTO.getDepartments().stream())
                .collect(Collectors.toSet());

        // 사원 및 부서를 한 번에 조회
        Map<String, Employee> employeeMap = findEmployeesByIds(empIds);
        Map<Long, Department> departmentMap = findDepartmentsByIds(departmentIds);

        List<DocShareObj> docShareObjs = new ArrayList<>();

        for(ShareDTO shareDTO : request.getShares()) {
            if (shareDTO.getShareEmpDeptType() == EmpDeptType.EMPLOYEE) {
                shareDTO.getEmployees().forEach(empId -> {
                    Employee employee = employeeMap.get(empId);
                    docShareObjs.add(DocMapper.toDocShareObj(doc, createUser, EmpDeptType.EMPLOYEE, employee, null));
                });
            } else if (shareDTO.getShareEmpDeptType() == EmpDeptType.DEPARTMENT) {
                shareDTO.getDepartments().forEach(departmentId -> {
                    Department department = departmentMap.get(departmentId);
                    docShareObjs.add(DocMapper.toDocShareObj(doc, createUser, EmpDeptType.DEPARTMENT, null, department));
                });
            } else {
                throw new CustomException(ErrorCode.INVALID_SHARE_TYPE);
            }
        }

        docShareObjCommandRepository.saveAll(docShareObjs);
    }

    // 결재선 리스트 추가
    private void createApproveLines(DocCreateRequestDTO request, Doc doc, Employee createUser) {

        for (ApproveLineRequestDTO lineDTO : request.getApproveLines()) {

            ApproveLine approveLine = createApproveLine(lineDTO, doc, createUser);
            doc.getApproveLines().add(approveLine);
        }
    }

    // 결재선 추가
    private ApproveLine createApproveLine(ApproveLineRequestDTO lineDTO, Doc doc, Employee createUser) {

        // 모든 empId, departmentId
        Set<String> empIds = lineDTO.getApproveSbjs().stream()
                .map(ApproveSbjDTO::getEmpId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> departmentIds = lineDTO.getApproveSbjs().stream()
                .map(ApproveSbjDTO::getDepartmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 모든 사원, 부서 조회할 때마다 DB 접근하지 않기 위해 한 번에 Map 을 사용하여 한 번에 조회
        Map<String, Employee> employeeMap = findEmployeesByIds(empIds);
        Map<Long, Department> departmentMap = findDepartmentsByIds(departmentIds);

        // groupId 설정
        Long groupId = (lineDTO.getApproveTemplateType() == MY_APPROVE_LINE)
                ? generateGroupId(null) // 새 group id 생성
                : doc.getDocId();   // 문서 id 를 group id 로

        // 새로운 결재선 생성
        ApproveLine newApproveLine = ApproveLine.builder()
                .doc(doc)
                .groupId(generateGroupId(groupId))
                .approveTemplateType(lineDTO.getApproveTemplateType())
                .createUser(createUser)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .build();

        // 결재 주체 추가
        lineDTO.getApproveSbjs().forEach(sbjDTO -> {
            ApproveSbj approveSbj = DocMapper.toApproveSbj(sbjDTO, employeeMap, departmentMap, lineDTO.getApproveType());
            newApproveLine.addApproveSbj(approveSbj);
        });

        return newApproveLine;
    }


    // 결재선의 group id 생성
    private Long generateGroupId(Long docId) {

        if (docId != null) {
            return docId;
        }

        Long maxGroupId = approveLineCommandRepository.findMaxGroupId();

        return (maxGroupId != null ? maxGroupId + 1 : 1L);
    }

    // 사용자가 입력한 필드 데이터 저장
    private void createDocFields(DocCreateRequestDTO request, Doc doc) {

        log.info("createDocFields 메소드 시작: ");

        request.getFields().forEach(field -> {

            log.info("field: " + field);

            // templateField 조회
            TemplateField templateField = findTemplateFieldById(field.getTemplateFieldId());

            DocField docField = DocField.builder()
                    .doc(doc)
                    .templateField(templateField)
                    .userValue(field.getUserValue().toString())
                    .build();

            docFieldCommandRepository.save(docField);
        });
    }

    private TemplateField findTemplateFieldById(Long templateFieldId) {
        log.info("Fetching TemplateField with ID: {}", templateFieldId);
        return templateFieldCommandRepository.findById(templateFieldId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE_FIELD));
    }


    // 문서 객체 생성
    private Doc createDoc(DocCreateRequestDTO request, Employee createUser, Template template) {

        return DocMapper.toEntity(request, createUser, template);
    }
    
    // 사원 조회
    private Employee findEmployeeById(String createUserId) {
        log.info("findEmployeeById 실행");
        return employeeCommandRepository.findById(createUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
    
    // 서식 조회
    private Template findTemplateById(Long templateId) {
        log.info("find(Template)ById 실행");
        return templateCommandRepository.findById(templateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE));
    }

    private Map<String, Employee> findEmployeesByIds(Set<String> empIds) {
        log.info("findEmployeeByIds 실행");
        return employeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
    }

    private Map<Long, Department> findDepartmentsByIds(Set<Long> departmentIds) {
        log.info("findDepartmentByIds 실행");
        return departmentCommandRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
    }

}
