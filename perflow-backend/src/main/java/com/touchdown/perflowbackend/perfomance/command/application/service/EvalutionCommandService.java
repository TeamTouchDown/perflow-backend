package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.*;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoHistory;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfoquestion;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoHistoryCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvalutionCommandService {

    private final PerfoCommandRepository perfoCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final PerfoQuestionCommandRepository perfoQuestionCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final HrPerfoCommandRepository hrPerfoCommandRepository;
    private final HrPerfoHistoryCommandRepository hrPerfoHistoryCommandRepository;

    // 동료 평가 생성
    @Transactional
    public void createPerfo(EvalutionListDTO evalutionListDTO, String empId) {

        // 평가자 정보 받아오기
        Employee perfoEmp = findEmployeeByEmpId(empId);

        // 피평가자 정보 받아오기
        Employee perfoedEmp = findEmployeeByEmpId(evalutionListDTO.getPerfoedEmpId());

        // 평가자와 피평가자의 부서가 일치하는지 확인
        if (isSameDepartment(perfoEmp.getDept().getDepartmentId() ,perfoedEmp.getDept().getDepartmentId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_DEPARTMENT);
        }

        // 평가 엔터티에 받아온 내용 매핑하기
        List<Perfo> perfos  = PerformanceMapper.evaluationAnswertoPerfo(evalutionListDTO, perfoEmp, perfoedEmp, perfoQuestionCommandRepository);

        perfoCommandRepository.saveAll(perfos);
    }

    // 동료 평가 수정
    @Transactional
    public void updatePerfo(EvalutionListDTO evalutionListDTO, String empId) {

        // 평가자 정보 받아오기
        Employee perfoEmp = findEmployeeByEmpId(empId);

        // 피평가자 정보 받아오기
        Employee perfoedEmp = findEmployeeByEmpId(evalutionListDTO.getPerfoedEmpId());

        // 평가자와 피평가자의 부서가 일치하는지 확인
        if (isSameDepartment(perfoEmp.getDept().getDepartmentId() ,perfoedEmp.getDept().getDepartmentId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_DEPARTMENT);
        }

        // 올해 년도와 일치하는 동료 평가 결과를 불러오기
        List<Perfo> perfoList = getCurrentYearPerfos(perfoEmp.getEmpId(), perfoedEmp.getEmpId());

        // 평가 엔터티에 받아온 내용 업데이트하기
        updatePerfoList(perfoList, evalutionListDTO);

        // 업데이트 된 내용 반영하기
        perfoCommandRepository.saveAll(perfoList);
    }

    // 동료 평가 문항 생성
    @Transactional
    public void createQuestion(String empId, CreateQuestionRequestDTO createQuestionRequestDTO) {

        // 문제관리자 존재하는지 확인
        Employee Emp = findEmployeeByEmpId(empId);

        // 문제 생성 부서 확인
        Department Dep = findDepartmentByEmpId(Emp.getDept().getDepartmentId());

        // 받아온 정보를 이용해 문제 생성
        Perfoquestion perfoquestion = PerformanceMapper.createQuestionRequestDTO(Emp, createQuestionRequestDTO, Dep);

        // 문제 저장
        perfoQuestionCommandRepository.save(perfoquestion);
    }

    // 동료 평가 문항 수정
    @Transactional
    public void updateQuestion(String empId, Long perfoQuestionId, UpdateQuestionRequestDTO updateQuestionRequestDTO) {

        // 문제관리자 존재하는지 확인
        Employee Emp = findEmployeeByEmpId(empId);

        // 기존 문제 정보 불러오기
        Perfoquestion perfoquestion = perfoQuestionCommandRepository.findByperfoQuestionId(perfoQuestionId);

        // 가져온 정보로 문제 업데이트 하기
        perfoquestion.updateQuestion(Emp, updateQuestionRequestDTO);

        // 수정된 정보 저장하기
        perfoQuestionCommandRepository.save(perfoquestion);
    }

    // 동료 평가 문항 삭제
    @Transactional
    public void deleteQuestion(String empId, Long questionPerfoId){

        // 문제관리자 존재하는지 확인
        Employee Emp = findEmployeeByEmpId(empId);

        // 문제 삭제
        perfoQuestionCommandRepository.deleteById(questionPerfoId);
    }

    // 평가 조정 생성
    @Transactional
    public void createPerfoAdjustment(String perfoId, String perfoedId, CreatePerfoAdjustmentDTO createPerfoAdjustmentDTO) {

        // 평가자 존재하는지 확인
        Employee perfoEmp = findEmployeeByEmpId(perfoId);

        // 피평가자 존재하는지 확인
        Employee perfoedEmp = findEmployeeByEmpId(perfoedId);

        // 평가 조정 생성
        HrPerfoHistory hrPerfoHistory = PerformanceMapper.perfoAdjustmentDTOToAdjustment(perfoEmp,perfoedEmp,createPerfoAdjustmentDTO);

        // 평가 조절 저장
        hrPerfoHistoryCommandRepository.save(hrPerfoHistory);
    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 받아온 DEPT id를 이용해 DEP 정보 불러오기
    private Department findDepartmentByEmpId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }

    // 평가자와 피평가자의 부서가 일치하는지 확인
    private boolean isSameDepartment(Long perfoempDepartment, Long perfoedempDepartment) {
        return !perfoempDepartment.equals(perfoedempDepartment);
    }

    //올해 년도와 일치하는 동료 평가 결과를 불러오기
    public List<Perfo> getCurrentYearPerfos(String perfoEmpId, String perfoEdEmpId) {
        // perfoEmpId와 perfoEdEmpId로 데이터 조회
        List<Perfo> perfos = perfoCommandRepository.findByPerfoEmp_EmpIdAndPerfoedEmp_EmpId(
                String.valueOf(perfoEmpId),
                String.valueOf(perfoEdEmpId)
        );

        // 현재 연도 가져오기
        int currentYear = java.time.Year.now().getValue();

        // 같은 년도의 데이터만 필터링
        return perfos.stream()
                .filter(perfo -> perfo.getCreateDatetime() != null &&
                        perfo.getCreateDatetime().getYear() == currentYear)
                .toList();
    }

    // 평가 엔터티에 받아온 내용 업데이트하기
    public void updatePerfoList(List<Perfo> perfoList, EvalutionListDTO evalutionListDTO) {
        // EvalutionDetailDTO 리스트를 Map으로 변환 (questionId를 키로 매핑)
        Map<Long, EvalutionDetailDTO> dtoMap = evalutionListDTO.getAnswers()
                .stream()
                .collect(Collectors.toMap(EvalutionDetailDTO::getQuestionId, dto -> dto));

        // perfoList를 순회하며 매핑된 DTO로 업데이트
        perfoList.forEach(perfo -> {
            // Perfo의 questionId와 EvalutionDetailDTO의 questionId 매핑
            Long questionId = perfo.getPerfoQuestion().getPerfoQuestionId();
            EvalutionDetailDTO evalutionDetailDTO = dtoMap.get(questionId);

            if (evalutionDetailDTO != null) {
                // Perfo 엔티티의 answer 업데이트
                perfo.updatePerfo(evalutionDetailDTO);
            }
        });
    }
}
