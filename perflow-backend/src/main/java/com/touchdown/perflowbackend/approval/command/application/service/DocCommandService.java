package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.*;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.*;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.ApproveSbjDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineResponseDTO;
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

    // 새 결재 문서 생성
    @Transactional
    public void createNewDoc(DocCreateRequestDTO request, String createUserId) {

        log.info("createNewDoc 실행");

        // 작성자 조회
        Employee createUser = findEmployeeById(createUserId);

        // 서식 조회
        Template template = findTemplateById(request.getTemplateId());

        // 문서 저장
        Doc doc = createDoc(request, createUser, template);
        docCommandRepository.save(doc);

        // 필드 데이터 저장
        createDocFields(request, doc);

        // 결재선 생성
        createApproveLines(request, doc, createUser);

        // 공유 설정
        createShare(request, doc, createUser);
    }

    // 나의 결재선 생성
    @Transactional
    public void createNewMyApproveLine(MyApproveLineCreateRequestDTO request, String createUserId) {

        // 작성자 조회
        Employee createUser = findEmployeeById(createUserId);

        Long groupId = generateGroupId(null);

        // 결재선 생성
        for(ApproveLineRequestDTO lineDTO : request.getApproveLines()) {
            ApproveLine approveLine = createApproveLineForMyApproveLine(lineDTO, createUser, groupId, request.getName(), request.getDescription());
            approveLineCommandRepository.save(approveLine);
        }
    }

    // 나의 결재선 수정
    @Transactional
    public void updateMyApproveLine(
            Long groupId,
            MyApproveLineUpdateRequestDTO request,
            String updateUserId) {

        List<ApproveLine> originLines = approveLineCommandRepository.findAllByGroupId(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE));

        // 기존 결재선 삭제 (approvesbj 도 삭제됨)
        approveLineCommandRepository.deleteAll(originLines);

        Employee updateUser = findEmployeeById(updateUserId);

        // 새로운 결재선 생성
        createNewMyApproveLineForUpdate(request, updateUserId, groupId);
    }

    @Transactional
    public void createNewMyApproveLineForUpdate(MyApproveLineUpdateRequestDTO request, String createUserId, Long groupId) {

        Employee createUser = findEmployeeById(createUserId);

        // 새로운 결재선 생성
        List<ApproveLine> approveLines = request.getApproveLines().stream()
                .map(lineDTO -> createApproveLineForMyApproveLine(lineDTO, createUser, groupId, request.getName(), request.getDescription()))
                .toList();

        approveLineCommandRepository.saveAll(approveLines);
    }

    // 나의 결재선 단일 삭제
    @Transactional
    public void deleteMyApproveLine(Long groupId, String deleteUserId) {

        // 결재선 조회
        List<ApproveLine> approveLines = approveLineCommandRepository.findAllByGroupId(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE));

        // 결재선 생성자와 현재 수정하려는 사용자 비교
        Employee deleteUser = findEmployeeById(deleteUserId);
        if (!approveLines.get(0).getCreateUser().equals(deleteUser)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

        approveLines.forEach(line -> {
            line.updateStatus(Status.DELETED);
            line.getApproveSbjs().forEach(sbj -> sbj.updateStatus(Status.DELETED));
        });

        approveLineCommandRepository.saveAll(approveLines);
    }

    // 나의 결재선 일괄 삭제
    @Transactional
    public void deleteMyApproveLines(BulkDeleteRequestDTO request, String deleteUserId) {

        // 결재선 생성자와 현재 수정하려는 사용자 비교
        Employee deleteUser = findEmployeeById(deleteUserId);

        // groupId 기반으로 결재선 조회
        List<ApproveLine> approveLines = approveLineCommandRepository.findAllByGroupIds(request.getGroupIds());

        if (approveLines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE);
        }

        approveLines.forEach(line -> {
            line.updateStatus(Status.DELETED);
            line.getApproveSbjs().forEach(sbj -> sbj.updateStatus(Status.DELETED)); // 각 결재 주체도 삭제
        });

        approveLineCommandRepository.saveAll(approveLines);
    }

    private ApproveLine createApproveLineForMyApproveLine(
            ApproveLineRequestDTO lineDTO,
            Employee createUser,
            Long groupId,
            String name,
            String description
    ) {

        // 모든 empId 추출
        Set<String> empIds = lineDTO.getApproveSbjs().stream()
                .map(ApproveSbjDTO::getEmpId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 모든 사원 조회
        Map<String, Employee> employeeMap = findEmployeesByIds(empIds);

        // 새로운 결재선 생성
        ApproveLine newApproveLine = ApproveLine.builder()
                .doc(null) // 문서와 연결되지 않음
                .groupId(groupId)
                .approveTemplateType(ApproveTemplateType.MY_APPROVE_LINE) // 나의 결재선
                .createUser(createUser)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .name(name) // 필요하면 그룹 이름 추가
                .description(description)
                .build();

        // 결재 주체 추가
        lineDTO.getApproveSbjs().forEach(sbjDTO -> {
            ApproveSbj approveSbj = DocMapper.toApproveSbj(sbjDTO, employeeMap, lineDTO.getApproveType());
            newApproveLine.addApproveSbj(approveSbj);
        });

        return newApproveLine;
    }

    private void createShare(DocCreateRequestDTO request, Doc doc, Employee createUser) {

        // 모든 empId와 departmentId 추출
        Set<String> empIds = request.getShares().stream()
                .filter(shareDTO -> shareDTO.getShareEmpDeptType() == EmpDeptType.EMPLOYEE)
                .flatMap(shareDTO -> shareDTO.getEmployees().stream())
                .collect(Collectors.toSet());

        // 사원을 한 번에 조회
        Map<String, Employee> employeeMap = findEmployeesByIds(empIds);

        List<DocShareObj> docShareObjs = new ArrayList<>();

        for(ShareDTO shareDTO : request.getShares()) {
            shareDTO.getEmployees().forEach(empId -> {
                Employee employee = employeeMap.get(empId);
                docShareObjs.add(DocMapper.toDocShareObj(doc, createUser, EmpDeptType.EMPLOYEE, employee));
            });
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


        // 모든 사원, 부서 조회할 때마다 DB 접근하지 않기 위해 한 번에 Map 을 사용하여 한 번에 조회
        Map<String, Employee> employeeMap = findEmployeesByIds(empIds);

        // groupId 설정
        Long groupId = (lineDTO.getApproveTemplateType() == MY_APPROVE_LINE)
                ? generateGroupId(null) // 새 group id 생성
                : doc.getDocId();   // 문서 id 를 group id 로

        // 새로운 결재선 생성
        ApproveLine newApproveLine = ApproveLine.builder()
                .doc(doc)
                .groupId(groupId)
                .approveTemplateType(lineDTO.getApproveTemplateType())
                .createUser(createUser)
                .approveType(lineDTO.getApproveType())
                .approveLineOrder(lineDTO.getApproveLineOrder())
                .pllGroupId(lineDTO.getPllGroupId())
                .build();

        // 결재 주체 추가
        lineDTO.getApproveSbjs().forEach(sbjDTO -> {
            ApproveSbj approveSbj = DocMapper.toApproveSbj(sbjDTO, employeeMap, lineDTO.getApproveType());
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

        request.getFields().forEach((fieldKey, userValue) -> {
            DocField docField = DocField.builder()
                    .doc(doc)
                    .fieldKey(fieldKey)
                    .userValue(userValue)
                    .build();
            docFieldCommandRepository.save(docField);
        });
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
}
