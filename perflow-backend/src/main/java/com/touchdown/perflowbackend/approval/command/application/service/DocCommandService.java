package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.*;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveLineCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocFieldCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.command.infrastructure.repository.JpaDocShareObjCommandRepository;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.ApproveSbjDTO;
import com.touchdown.perflowbackend.approval.query.service.TemplateQueryService;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.employee.command.infrastructure.repository.JpaEmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.infrastructure.repository.JpaDepartmentCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private final TemplateQueryService templateQueryService;
    private final DocFieldCommandRepository docFieldCommandRepository;
    private final JpaEmployeeCommandRepository jpaEmployeeCommandRepository;
    private final JpaDepartmentCommandRepository jpaDepartmentCommandRepository;
    private final JpaDocShareObjCommandRepository jpaDocShareObjCommandRepository;

    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        // 사원 조회
        Employee createUser = findEmployeeById(createUserId);

        // 서식 조회
        Template template = findTemplateById(request.getTemplateId());

        // 문서 저장
        Doc doc = createDoc(request, createUser, template);

        // 필드 데이터 설정
        createDocFields(request, doc);

        // 결재선 생성
        createApproveLines(request, doc, createUser);

        // 공유 설정
        createShare(request, doc, createUser);

        docCommandRepository.save(doc);
    }

    // 사용자가 입력한 필드 데이터 저장
    private void createDocFields(DocCreateRequestDTO request, Doc doc) {

        request.getFields().forEach(field -> {

            docFieldCommandRepository.save(
                    DocField.builder()
                            .doc(doc)
                            .templateField(field.getTemplateField())
                            .userValue(field.getUserValue().toString())
                            .build()
            );
        });
    }

    @Transactional
    public void createNewMyApproveLine(MyApproveLineCreateRequestDTO request, String createUserId) {

        Employee createUser = findEmployeeById(createUserId);

        Long groupId = generateGroupId(null);
        
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

    private Long generateGroupId(Long docId) {

        if (docId != null) {
            return docId;
        }

        Long maxGroupId = approveLineCommandRepository.findMaxGroupId();

        return (maxGroupId != null ? maxGroupId + 1 : 1L);
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
        Map<String, Employee> employeeMap = jpaEmployeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));

        Map<Long, Department> departmentMap = jpaDepartmentCommandRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

        for (ShareDTO shareDTO : request.getShares()) {

            // 공유 대상이 사원인 경우
            if (shareDTO.getShareEmpDeptType() == EmpDeptType.EMPLOYEE) {
                shareDTO.getEmployees().forEach(empId -> {
                    Employee employee = employeeMap.get(empId);
                    if (employee == null) {
                        throw new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE);
                    }
                    DocShareObj docShareObj = DocShareObj.builder()
                            .doc(doc)
                            .shareAddUser(createUser)
                            .shareEmpDeptType(EmpDeptType.EMPLOYEE)
                            .shareObjUser(employee)
                            .build();

                    docShareObj.add(docShareObj);
                });
                // 공유 대상이 부서인 경우
            } else if (shareDTO.getShareEmpDeptType() == EmpDeptType.DEPARTMENT) {
                shareDTO.getDepartments().forEach(departmentId -> {
                            Department department = departmentMap.get(departmentId);
                            if (department == null) {
                                throw new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT);
                            }

                            DocShareObj docShareObj = DocShareObj.builder()
                                    .doc(doc)
                                    .shareAddUser(createUser)
                                    .shareEmpDeptType(EmpDeptType.DEPARTMENT)
                                    .shareObjDepartment(department)
                                    .build();
                            docShareObj.add(docShareObj);
                });
            }  else {
                throw new CustomException(ErrorCode.INVALID_SHARE_TYPE);
        }


    }

    private DocShareObj createShareObj(Doc doc, EmpDeptType EmpDeptType, Employee shareUser, Department shareDepartment, Employee createUser) {

        return DocShareObj.builder()
                .doc(doc)
                .shareEmpDeptType(EmpDeptType)
                .shareObjUser(shareUser)
                .shareObjDepartment(shareDepartment)
                .shareAddUser(createUser)
                .build();
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
        Map<String, Employee> employeeMap = jpaEmployeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
        Map<Long, Department> departmentMap = jpaDepartmentCommandRepository.findAllById(departmentIds).stream()
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

    // 문서 객체 생성
    private Doc createDoc(DocCreateRequestDTO request, Employee createUser, Template template) {

        return DocMapper.toEntity(request, createUser, template);
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
        Map<String, Employee> employeeMap = jpaEmployeeCommandRepository.findAllById(empIds).stream()
                .collect(Collectors.toMap(Employee::getEmpId, Function.identity()));
        Map<Long, Department> departmentMap = jpaDepartmentCommandRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

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
        for (ApproveSbjDTO sbjDTO : lineDTO.getApproveSbjs()) {
            Employee sbjUser = null;
            if (sbjDTO.getEmpId() != null) {
                sbjUser = employeeMap.get(sbjDTO.getEmpId());
                if (sbjUser == null) {
                    throw new CustomException(ErrorCode.NOT_FOUND_EMP);
                }
            }

            Department dept = null;
            if (sbjDTO.getDepartmentId() != null) {
                dept = departmentMap.get(sbjDTO.getDepartmentId());
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

    // 결재 주체 추가
    private void addApproveSbjs(List<ApproveSbj> sbjs, ApproveLine approveLine) {

        // 결재 주체가 사원인 경우
        for (String empId : sbjs.getEmployees()) {
            Employee employee = findEmployeeById(empId);
            ApproveSbj approveSbj = createApproveSbj(approveLine, EmpDeptType.EMPLOYEE, employee, null, isParallel(lineDTO));
            approveLine.getApproveSubjects().add(approveSbj);
        }

        // 결재 주체가 부서인 경우
        for (Long deptId : lineDTO.getDepartments()) {
            Department department = findDepartmentByID(deptId);
            ApproveSbj approveSbj = createApproveSbj(approveLine, EmpDeptType.DEPARTMENT, null, department, isParallel(lineDTO));
            approveLine.getApproveSubjects().add(approveSbj);
        }
    }

    private ApproveSbj createApproveSbj(ApproveLine approveLine, EmpDeptType empDeptType, Employee sbjUser, Department dept, boolean isParallel) {

        return ApproveSbj.builder()
                .approveLine(approveLine)
                .empDeptType(empDeptType)
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
