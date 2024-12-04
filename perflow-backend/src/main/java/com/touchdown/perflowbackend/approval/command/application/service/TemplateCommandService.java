package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.FieldType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.TemplateField;
import com.touchdown.perflowbackend.approval.command.domain.repository.FieldTypeCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateFieldCommandRepository;
import com.touchdown.perflowbackend.approval.command.infrastructure.repository.JpaFieldTypeCommandRepository;
import com.touchdown.perflowbackend.approval.command.mapper.TemplateFieldMapper;
import com.touchdown.perflowbackend.approval.command.mapper.TemplateMapper;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateCommandService {
    private final TemplateCommandRepository templateCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final TemplateFieldCommandRepository templateFieldCommandRepository;
    private final JpaFieldTypeCommandRepository jpaFieldTypeCommandRepository;

    @Transactional
    public TemplateCreateResponseDTO createNewTemplate(TemplateCreateRequestDTO request, String empId) {

        Employee createEmp = findEmployeeById(empId);

        Template savedTemplate = saveTemplate(createEmp, request);

        validateFieldData(request);

        List<FieldType> fieldTypes = fetchFieldTypes(request.getFieldTypes());

        List<TemplateField> fields = createTemplateFields(savedTemplate, fieldTypes, request);

        saveTemplateFields(fields);

        return new TemplateCreateResponseDTO(savedTemplate.getTemplateId());
    }

    // 사원 조회
    private Employee findEmployeeById(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 서식 엔터티 생성, 저장
    private Template saveTemplate(Employee createEmp, TemplateCreateRequestDTO request) {
        Template newTemplate = TemplateMapper.templateToEntity(createEmp, request);
        Template savedTemplate = templateCommandRepository.save(newTemplate);
        log.info("서식 생성 성공 - TemplateId: {}", savedTemplate.getTemplateId());
        return savedTemplate;
    }

    // 필드 데이터 유효성 검사
    private void validateFieldData(TemplateCreateRequestDTO request) {
        List<Long> fieldTypeIds = request.getFieldTypes();
        List<String> detailsList = request.getDetailsList();
        List<Boolean> isRepeatedList = request.getIsRepeatedList();

        if (fieldTypeIds == null || detailsList == null || isRepeatedList == null ||
                fieldTypeIds.size() != detailsList.size() || fieldTypeIds.size() != isRepeatedList.size()) {
            log.info("필드 데이터의 크기가 일치하지 않음");
            throw new CustomException(ErrorCode.INVALID_FIELD_DATA);
        }
    }

    // 필요한 모든 필드 타입 한 번에 가져오기
    private List<FieldType> fetchFieldTypes(List<Long> fieldTypeIds) {
        List<FieldType> fieldTypes = jpaFieldTypeCommandRepository.findAllById(fieldTypeIds);
        if (fieldTypes.size() != fieldTypeIds.stream().distinct().count()) {
            throw new CustomException(ErrorCode.NOT_FOUND_FIELD_TYPE);
        }
        return fieldTypes;
    }

    // TemplateField 엔터티 생성
    private List<TemplateField> createTemplateFields(Template template, List<FieldType> fieldTypes, TemplateCreateRequestDTO request) {
        List<Long> fieldTypeIds = request.getFieldTypes();
        List<String> detailsList = request.getDetailsList();
        List<Boolean> isRepeatedList = request.getIsRepeatedList();

        Map<Long, FieldType> fieldTypeMap = fieldTypes.stream()
                .collect(Collectors.toMap(FieldType::getFieldTypeId, ft -> ft));

        return IntStream.range(0, fieldTypeIds.size())
                .mapToObj(i -> {
                    FieldType fieldType = fieldTypeMap.get(fieldTypeIds.get(i));
                    if (fieldType == null) {
                        throw new CustomException(ErrorCode.NOT_FOUND_FIELD_TYPE);
                    }
                    return TemplateFieldMapper.templateFieldToEntity(
                            template,
                            fieldType,
                            detailsList.get(i),
                            isRepeatedList.get(i),
                            (long) i + 1
                    );
                })
                .toList();
    }

    // 필드 저장
    private void saveTemplateFields(List<TemplateField> fields) {
        templateFieldCommandRepository.saveAll(fields);
        log.info("서식 필드 저장 성공: 필드 수 {}", fields.size());
    }
}
