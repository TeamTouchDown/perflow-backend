package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.QuestionType;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaAnswerResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.EvaQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.EvaQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaQueryService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final EvaQueryRepository evaQueryRepository;

    // 동료 평가 리스트 조회
    @Transactional(readOnly = true)
    public List<EvaDetailResponseDTO> getEvaColList(String empId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 현재 유저의 부서 체크하기
        Department dep = findDepartmentByDeptId(emp.getDept().getDepartmentId());

        // 현재 유저의 동료 리스트 저장
        List<EvaDetailResponseDTO> evaList = evaQueryRepository.findEmployeeDetailsWithExistence(dep.getDepartmentId(), empId, java.time.Year.now().getValue(),PerfoType.valueOf("COL")) ;

        return evaList;
    }

    // 동료 평가 정답 조회
    @Transactional(readOnly = true)
    public List<EvaAnswerResponseDTO> getEvaColAnswer(String perfoempId, String perfoedempId) {

        // 유저가 존재하는지 체크하기
        Employee perfoemp = findEmployeeByEmpId(perfoempId);

        // 유저가 존재하는지 체크하기
        Employee perfoedemp = findEmployeeByEmpId(perfoedempId);

        // 현재 지정된 동료 평가의 답변 불러오기
        List<EvaAnswerResponseDTO> evaAnswerList = evaQueryRepository.findAnswerByEmpIds(perfoempId, perfoedempId, java.time.Year.now().getValue(), PerfoType.valueOf("COL"));

        return evaAnswerList;
    }

    // 동료 평가 정답 조회
    @Transactional(readOnly = true)
    public List<String> getEvaColAnswers(String perfoedempId, Long questionId, Long deptId) {

        // 유저가 존재하는지 체크하기
        Employee perfoedemp = findEmployeeByEmpId(perfoedempId);

        // 현재 지정된 동료 평가의 답변 불러오기
        List<String> evaAnswerList = evaQueryRepository.findQuestionByempId(deptId, PerfoType.valueOf("COL"), QuestionType.valueOf("SUBJECTIVE"), java.time.Year.now().getValue(), perfoedempId, questionId);

        return evaAnswerList;
    }

    // 동료 평가 정답 조회
    @Transactional(readOnly = true)
    public List<String> getEvaDownAnswers(String perfoedempId, Long questionId, Long deptId) {

        // 유저가 존재하는지 체크하기
        Employee perfoedemp = findEmployeeByEmpId(perfoedempId);

        // 현재 지정된 동료 평가의 답변 불러오기
        List<String> evaAnswerList = evaQueryRepository.findQuestionByempId(deptId, PerfoType.valueOf("DOWN"), QuestionType.valueOf("SUBJECTIVE"), java.time.Year.now().getValue(), perfoedempId, questionId);

        return evaAnswerList;
    }

    // 평가 문제 리스트 조회
    @Transactional(readOnly = true)
    public List<EvaQuestionDetailResponseDTO> getEvaQuestionList(String empId, EvaQuestionRequestDTO evaQuestionRequestDTO){

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 조건에 맞춰 문항 불러오기
        List<EvaQuestionDetailResponseDTO> evaQuestionList = evaQueryRepository
                .findQuestionBydept(evaQuestionRequestDTO.getDeptId(),
                        PerfoType.valueOf(evaQuestionRequestDTO.getPerfoType()),
                        QuestionType.valueOf(evaQuestionRequestDTO.getQuestionType()),
                        java.time.Year.now().getValue()
                );

        return evaQuestionList;
    }

    // 하향 평가 리스트 조회
    @Transactional(readOnly = true)
    public List<EvaDetailResponseDTO> getEvaDownList(String empId) {

        // 유저가 존재하는지 체크하기
        Employee emp = findEmployeeByEmpId(empId);

        // 현재 유저의 부서 체크하기
        Department dep = findDepartmentByDeptId(emp.getDept().getDepartmentId());

        // 현재 유저의 하위 직원 리스트 저장
        List<EvaDetailResponseDTO> evaList = evaQueryRepository.findEmployeeDetailsWithExistence(dep.getDepartmentId(), empId, java.time.Year.now().getValue(), PerfoType.valueOf("DOWN")) ;

        return evaList;
    }

    // 하향 평가 정답 조회
    @Transactional(readOnly = true)
    public List<EvaAnswerResponseDTO> getEvaDownAnswer(String perfoempId, String perfoedempId) {

        // 유저가 존재하는지 체크하기
        Employee perfoemp = findEmployeeByEmpId(perfoempId);

        // 유저가 존재하는지 체크하기
        Employee perfoedemp = findEmployeeByEmpId(perfoedempId);

        // 현재 지정된 하향 평가의 답변 불러오기
        List<EvaAnswerResponseDTO> evaAnswerList = evaQueryRepository.findAnswerByEmpIds(perfoempId, perfoedempId, java.time.Year.now().getValue(),PerfoType.valueOf("DOWN"));

        return evaAnswerList;
    }

    // 동료 평가 점수 조회
    @Transactional(readOnly = true)
    public Long getEvaColScore(String empId){

        Employee emp = findEmployeeByEmpId(empId);

        List<Long> scoreList = evaQueryRepository.findScoreByEmpId(empId, PerfoType.valueOf("COL"), java.time.Year.now().getValue());

        // 점수 리스트가 비어있거나 null인 경우 평균은 0으로 설정
        if (scoreList == null || scoreList.isEmpty()) {
            return 0L;
        }

        // 점수의 평균 계산
        double average = scoreList.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        // 평균을 반올림하여 Long 타입으로 반환
        return Math.round(average * 20);
    }

    // 동료 평가 점수 조회
    @Transactional(readOnly = true)
    public Long getEvaDownScore(String empId){

        Employee emp = findEmployeeByEmpId(empId);

        List<Long> scoreList = evaQueryRepository.findScoreByEmpId(empId, PerfoType.valueOf("DOWN"), java.time.Year.now().getValue());

        // 점수 리스트가 비어있거나 null인 경우 평균은 0으로 설정
        if (scoreList == null || scoreList.isEmpty()) {
            return 0L;
        }

        // 점수의 평균 계산
        double average = scoreList.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        // 평균을 반올림하여 Long 타입으로 반환
        return Math.round(average * 20);
    }
    // 사번으로 사원 정보 찾기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 부서번호로 부서 정보 찾기
    private Department findDepartmentByDeptId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }
}
