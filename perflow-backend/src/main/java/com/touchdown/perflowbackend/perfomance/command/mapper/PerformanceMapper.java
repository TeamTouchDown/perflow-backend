package com.touchdown.perflowbackend.perfomance.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.perfomance.command.application.dto.*;
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
                .period(kpiDetailRequestDTO.getPeriod())
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

    // 인사 평가 생성(등급 생성 전)
    public static HrPerfo finalHrPerfo(Employee emp, Double finalScore){

        return HrPerfo.builder()
                .emp(emp)
                .score(finalScore)
                .status(HrPerfoStatus.WAIT)
                .build();
    }

    // 인사 평가 의의 제기 생성
    public static HrPerfoInquiry createhrPerfoInquiry(HrPerfo hrPerfo, String reason){

        return HrPerfoInquiry.builder()
                .hrPerfo(hrPerfo)
                .reason(reason)
                .status(PassStatus.APPROVAL)
                .build();
    }

    // 인사 평가 비율 생성
    public static Weight perforatioDTOtoWeight(Employee emp, Department dept, CreatePerfoRatioRequestDTO createPerfoRatioRequestDTO) {

        return Weight.builder()
                .emp(emp)
                .dept(dept)
                .personalWeight(createPerfoRatioRequestDTO.getPersonalKpiWeight())
                .teamWeight(createPerfoRatioRequestDTO.getTeamKpiWeight())
                .colWeight(createPerfoRatioRequestDTO.getColWeight())
                .downwardWeight(createPerfoRatioRequestDTO.getDownWeight())
                .attendanceWeight(createPerfoRatioRequestDTO.getAttendanceWeight())
                .reason(createPerfoRatioRequestDTO.getReason())
                .build();
    }

    // 등급 비율 생성
    public static GradeRatio GraderatioDTOtoGradeRatio(Employee emp, CreateGradeRatioRequestDTO createGradeRatioRequestDTO) {

        return GradeRatio.builder()
                .emp(emp)
                .sRatio(createGradeRatioRequestDTO.getSRatio())
                .aRatio(createGradeRatioRequestDTO.getARatio())
                .bRatio(createGradeRatioRequestDTO.getBRatio())
                .cRatio(createGradeRatioRequestDTO.getCRatio())
                .dRatio(createGradeRatioRequestDTO.getDRatio())
                .reason(createGradeRatioRequestDTO.getReason())
                .build();
    }

    // KPI 처리 생성
    public static KpiProgressStatus kpipassDTOtokpiProgress(Employee emp, Kpi kpi, CreateKpiProgressDTO createKpiProgressDTO) {

        return KpiProgressStatus.builder()
                .kpi(kpi)
                .emp(emp)
                .progressStatus(createKpiProgressDTO.getProgress())
                .updateReason(createKpiProgressDTO.getReason())
                .build();
    }

    // 평가 조정 생성
    public static HrPerfoHistory perfoAdjustmentDTOToAdjustment(Employee perfoEmp, Employee perfoedEmp, CreatePerfoAdjustmentDTO createPerfoAdjustmentDTO) {

        return HrPerfoHistory.builder()
                .perfo_emp(perfoEmp)
                .perfoed_emp(perfoedEmp)
                .adjustmentColScore(createPerfoAdjustmentDTO.getColScore())
                .adjustmentDownScore(createPerfoAdjustmentDTO.getDownScore())
                .adjustmentDegree(createPerfoAdjustmentDTO.getDegree())
                .reason(createPerfoAdjustmentDTO.getReason())
                .build();
    }

    // AI 요약 생성
    public static AiPerfoSummary createAiSummary(Employee emp, PerfoType perfoType, String summary){

        return AiPerfoSummary.builder()
                .emp(emp)
                .perfoType(perfoType)
                .aiSummary(summary)
                .build();
    }

    // KPI 공개
    public static KpiStatus createKpiStatus(Employee emp, Kpi kpi, CreateKpiPassDTO createKpiPassDTO) {

        return KpiStatus.builder()
                .emp(emp)
                .kpi(kpi)
                .passStatus(PassStatus.valueOf(createKpiPassDTO.getStatus()))
                .passReason(createKpiPassDTO.getReason())
                .build();
    }
}
