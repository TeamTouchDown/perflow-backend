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

import static com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType.MANUAL;
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
            ApproveLine approveLine;

            if (lineDTO.getApproveTemplateTypes() == MY_APPROVE_LINE) { // 나의 결재선에서 불러온 경우
                approveLine = loadMyApproveLine(lineDTO, doc, createUser);
            } else if (lineDTO.getApproveTemplateTypes() == MANUAL) {   // 결재 문서 작성 시 결재선을 직접 설정한 경우
                approveLine = createApproveLine(lineDTO, doc, createUser);
            } else {
                throw new CustomException(ErrorCode.INVALID_APPROVE_TEMPLATE_TYPE);
            }
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

        ApproveLine newApproveLine = ApproveLine.builder()
                .doc(doc)
                .groupId(generateGroupId(doc.getDocId()))    // 문서 id 를 group id 로
                .approveTemplateType(lineDTO.getApproveTemplateTypes())
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

    // 나의 결재선 불러오기
    private ApproveLine loadMyApproveLine(ApproveLineRequestDTO lineDTO, Doc doc, Employee createUser) {

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
        Map<String, Employee> employeeMap = employeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
        Map<Long, Department> departmentMap = departmentCommandRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

        // 나의 결재선 조회
        List<ApproveLine> myApproveLines = approveLineCommandRepository.findAllByGroupId(lineDTO.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE));

        // 요청된 나의 결재선 데이터와 기존 나의 결재선 데이터 비교
        if (isApproveLineModified(myApproveLines, lineDTO)) {
            // 기존 나의 결재선과 다르면 새로운 결재선 생성
            ApproveLine newApproveLine = ApproveLine.builder()
                    .doc(doc)
                    .approveType(lineDTO.getApproveType())
                    .approveLineOrder(lineDTO.getApproveLineOrder())
                    .pllGroupId(lineDTO.getPllGroupId())
                    .groupId(generateGroupId(null)) //새 groupId 생성
                    .createUser(createUser)
                    .build();

            for (ApproveSbjDTO sbjDTO : lineDTO.getApproveSbjs()) {
                Employee sbjUser = null;
                if (sbjDTO.getEmpId() != null) {    // 사원 id 가 null 이 아니면
                    sbjUser = employeeMap.get(sbjDTO.getEmpId());   // Map 에서 Employee 가져옴
                    if (sbjUser == null) {
                        throw new CustomException(ErrorCode.NOT_FOUND_EMP);
                    }
                }

                Department dept = null;
                if (sbjDTO.getDepartmentId() != null) { // 부서 id 가 null 이 아니면
                    dept = departmentMap.get(sbjDTO.getDepartmentId()); // Map 에서 Department 가져옴
                    if (dept == null) {
                        throw new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT);
                    }
                }

                ApproveSbj approveSbj = ApproveSbj.builder()
                        .empDeptType(sbjDTO.getEmpDeptType())
                        .sbjUser(sbjUser) // Employee 설정
                        .dept(dept)       // Department 설정
                        .build();
                newApproveLine.addApproveSbj(approveSbj); // 관계 설정
            }
            return newApproveLine;
        }

        // 기존 나의 결재선 데이터와 동일하면 복사
        ApproveLine newApproveLine = ApproveLine.builder()
                .doc(doc)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .groupId(lineDTO.getGroupId())  // 기존 groupId 유지
                .createUser(createUser)
                .build();

        // 기존 ApproveSbjs 복사
        for (ApproveSbj originalSbj : myApproveLines.get(0).getApproveSbjs()) {
            ApproveSbj copiedSbj = ApproveSbj.builder()
                    .empDeptType(originalSbj.getEmpDeptType())
                    .sbjUser(originalSbj.getSbjUser()) // Employee 복사
                    .dept(originalSbj.getDept())       // Department 복사
                    .build();
            newApproveLine.addApproveSbj(copiedSbj); // 관계 설정
        }

        return newApproveLine;
    }

    // 결재선 변경 확인
    private boolean isApproveLineModified(List<ApproveLine> myApproveLines, ApproveLineRequestDTO lineDTO) {

        boolean matched = myApproveLines.stream()
                .anyMatch(line ->
                        line.getApproveType().equals(lineDTO.getApproveType()) &&
                                line.getApproveLineOrder().equals(lineDTO.getApproveLineOrder()) &&
                                isApproveSbjsModified(line.getApproveSbjs(), lineDTO.getApproveSbjs())
                );
        // 모든 데이터가 동일하지 않으면 false
        return !matched;
    }

    // 결재 주체 변경 확인
    private boolean isApproveSbjsModified(List<ApproveSbj> originalApproveSbjs, List<ApproveSbjDTO> newApproveSbjs) {

        if (originalApproveSbjs.size() != newApproveSbjs.size()) {
            return true;
        }

        for (int i = 0; i < originalApproveSbjs.size(); i++) {
            ApproveSbj originalSbj = originalApproveSbjs.get(i);
            ApproveSbjDTO newSbj = newApproveSbjs.get(i);

            if (!originalSbj.getEmpDeptType().equals(newSbj.getEmpDeptType()) ||
                    !Objects.equals(originalSbj.getSbjUser().getEmpId(), newSbj.getEmpId()) ||
                    !Objects.equals(originalSbj.getDept().getDepartmentId(), newSbj.getDepartmentId())) {
                return true;
            }
        }

        return false;
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

    // 결재 방식이 병렬/병렬 합의인지
    private Boolean isParallel(ApproveLineRequestDTO lineDTO) {

        return lineDTO.getApproveType() == ApproveType.PLL || lineDTO.getApproveType() == ApproveType.PLLAGR;
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

        return employeeCommandRepository.findById(createUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }
    
    // 서식 조회
    private Template findTemplateById(Long templateId) {

        return templateCommandRepository.findById(templateId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE));
    }

    private Map<String, Employee> findEmployeesByIds(Set<String> empIds) {

        return employeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
    }

    private Map<Long, Department> findDepartmentsByIds(Set<Long> departmentIds) {

        return departmentCommandRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
    }

}
