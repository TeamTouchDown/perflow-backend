package com.touchdown.perflowbackend.perfomance.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.perfomance.command.application.dto.CreateQuestionRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.application.dto.EvalutionListDTO;
import com.touchdown.perflowbackend.perfomance.command.application.dto.KPIDetailRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.*;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoQuestionCommandRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIListResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PerformanceMapper {

    // 개인 kpi 리스트 및 제한 DTO 빌더
    public static KPIListResponseDTO kpiListToDTO(List<KPIDetailResponseDTO> lists, KPILimitResponseDTO limit) {


        return KPIListResponseDTO.builder()
                .kpiLists(lists)    // 개인 kpi 목록
                .totalKpis((long) lists.size())    // 현재 kpi 갯수
                .minKpis(limit.getMinKpis())          // kpi 최소 제한 갯수
                .maxKpis(limit.getMaxKpis())          // kpi 최대 제한 갯수
                .build();
    }

    // 개인 kpi 저장용 엔터티 빌더
    public static Kpi kpiDTOtoEntity(KPIDetailRequestDTO kpiDetailRequestDTO, Employee emp, PersonalType personalType) {

        return Kpi.builder()
                .emp(emp)                                                   // 사원 정보
                .goal(kpiDetailRequestDTO.getGoal())                        // 받아온 목표 설정
                .goalValue(kpiDetailRequestDTO.getGoalValue())              // 받아온 목표치 설정
                .goalValueUnit(kpiDetailRequestDTO.getGoalValueUnit())      // 받아온 목표치 단위 설정
                .currentValue(0.0)                                          // 아직 시작되지 않은 KPI이므로 현황은 0
                .status(KpiCurrentStatus.WAIT)                              // 아직 승인 대기중인 KPI이므로 상태는 대기
                .personalType(personalType)                                 // 받아온 개인,팀 여부 설정
                .goalDetail(kpiDetailRequestDTO.getGoalDetail())            // 받아온 목표 상세 설정
                .build();
    }

    // 평가 문제 정답 생성
    public static List<Perfo> evaluationAnswertoPerfo(EvalutionListDTO evalutionListDTO, Employee perfoEmp, Employee perfoedEmp, PerfoQuestionCommandRepository perfoQuestionCommandRepository) {

        return evalutionListDTO.getAnswers().stream()
                .map(answer -> Perfo.builder()
                        .perfoQuestion(perfoQuestionCommandRepository.findByperfoQuestionId(answer.getQuestionId()))
                        .perfoEmp(perfoEmp)
                        .perfoedEmp(perfoedEmp)
                        .answer(answer.getAnswer())
                        .build())
                .collect(Collectors.toList());
    }

    // 평가 문제 생성
    public static Perfoquestion createQuestionRequestDTO(Employee emp, CreateQuestionRequestDTO createQuestionRequestDTO, Department department) {

        return Perfoquestion.builder()
                .dept(department)
                .emp(emp)
                .questionType(QuestionType.valueOf(createQuestionRequestDTO.getQuestionType()))
                .questionContent(createQuestionRequestDTO.getQuestionContent())
                .perfoType(PerfoType.valueOf(createQuestionRequestDTO.getPerfoType()))
                .build();
    }
}
