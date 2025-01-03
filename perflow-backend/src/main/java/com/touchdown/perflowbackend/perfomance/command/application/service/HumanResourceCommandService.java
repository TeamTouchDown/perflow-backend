package com.touchdown.perflowbackend.perfomance.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.application.dto.UpdateInquiryRequestDTO;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.*;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.WeightCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.infrastructure.repository.HrPerfoHistoryRepository;
import com.touchdown.perflowbackend.perfomance.command.infrastructure.repository.HrPerfoInquiryRepository;
import com.touchdown.perflowbackend.perfomance.command.mapper.PerformanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HumanResourceCommandService {

    private final KpiCommandRepository kpiCommandRepository;
    private final HrPerfoHistoryRepository hrPerfoHistoryRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final WeightCommandRepository weightCommandRepository;
    private final HrPerfoCommandRepository hrPerfoCommandRepository;
    private final HrPerfoInquiryRepository hrPerfoInquiryRepository;

    // 인사 평가 생성
    public void createHumanResource(String empId){

        // 평가자 정보 받아오기
        Employee Emp = findEmployeeByEmpId(empId);

        // 최종 점수 계산
        Double finalScore = getfinalScore(Emp);

        // 사용자 정보와 점수를 이용한 인사 평가 생성
        HrPerfo finalHrPerfo = PerformanceMapper.finalHrPerfo(Emp, finalScore);

        // 저장
        hrPerfoCommandRepository.save(finalHrPerfo);
    }

    // 인사 평가 수정
    public void updateHumanResource(String empId,Double score){

        // 평가자 정보 받아오기
        Employee Emp = findEmployeeByEmpId(empId);

        // 평가자의 사전 인사평가 받아오기
        HrPerfo hrPerfo = findHrPerfoByEmpId(empId);

        // 점수 업데이트
        hrPerfo.updateHrPerfo(score);

        // 저장
        hrPerfoCommandRepository.save(hrPerfo);
    }

    // 인사 평가 의의 제기 생성
    public void createHrPerfoInquiry(String empId, Long hrperfoId, String reason){

        // 인사 평가 정보 받아오기
        HrPerfo hrPerfo = findHrPerfoByhrPerfoId(hrperfoId);

        // 의의제기 작성자와 평가 받은 사람이 같은 사람인지 체크
        checkEmpEquals(empId, hrPerfo.getEmp().getEmpId());

        // 인사 평가 의의제기 생성
        HrPerfoInquiry hrPerfoInquiry = PerformanceMapper.createhrPerfoInquiry(hrPerfo, reason);

        // 저장
        hrPerfoInquiryRepository.save(hrPerfoInquiry);
    }

    // 인사 평가 의의 제기 수정(승인)
    public void updateHrPerfoInquiry(String empId, Long hrperfoInquiryId, UpdateInquiryRequestDTO updateInquiryRequestDTO){

        // 평가자 정보 받아오기
        Employee Emp = findEmployeeByEmpId(empId);

        // 인사 평가 의의제기 정보 받아오기
        HrPerfoInquiry hrPerfoinquiry = findHrPerfoInquiryByhrPerfoInquiryId(hrperfoInquiryId);

        // 인사 평가 점수 받아오기
        HrPerfo hrPerfo = findHrPerfoByhrPerfoId(hrPerfoinquiry.getHrPerfo().getHrPerfoId());

        // 인사 평가 의의제기 정보 업데이트
        hrPerfoinquiry.updateinquiry(Emp, updateInquiryRequestDTO);

        // 인사 평가 점수 업데이트
        hrPerfo.updateHrPerfo(updateInquiryRequestDTO);

        // 인사 평가 의의제기 정보 저장
        hrPerfoInquiryRepository.save(hrPerfoinquiry);

        // 인사 평가 정보 저장
        hrPerfoCommandRepository.save(hrPerfo);

    }

    // 인사 평가 의의 제기 수정(반려)
    public void updateHrPerfoInquiry(String empId, Long hrperfoInquiryId, String reason){

        // 평가자 정보 받아오기
        Employee Emp = findEmployeeByEmpId(empId);

        // 인사 평가 의의제기 정보 받아오기
        HrPerfoInquiry hrPerfoinquiry = findHrPerfoInquiryByhrPerfoInquiryId(hrperfoInquiryId);

        // 인사 평가 의의제기 정보 업데이트
        hrPerfoinquiry.updateinquiry(Emp, reason);

        // 인사 평가 의의제기 정보 저장
        hrPerfoInquiryRepository.save(hrPerfoinquiry);

    }

    // 받아온 EMP id를 이용해 EMP 정보 불러오기
    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));
    }

    // 받아온 EMP id를 이용해 인사 평가 정보 불러오기
    private HrPerfo findHrPerfoByEmpId(String empId) {
        int currentYear = Year.now().getValue(); // 현재 연도 가져오기
        return hrPerfoCommandRepository.findByEmpIdAndCurrentYear(empId, currentYear)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HRPERFO));
    }

    // 받아온 인사 평가 Id를 이용해 인사 평가 정보 불러오기
    private HrPerfo findHrPerfoByhrPerfoId(Long hrPerfoId) {
        return hrPerfoCommandRepository.findByhrPerfoId(hrPerfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HRPERFO));
    }

    // 받아온 인사 평가 의의제기 Id를 이용해 인사 평가 의의제기 정보 불러오기
    private HrPerfoInquiry findHrPerfoInquiryByhrPerfoInquiryId(Long hrPerfoInquiryId) {
        return hrPerfoInquiryRepository.findByhrPerfoInquiryId(hrPerfoInquiryId)
                .orElseThrow(() -> new CustomException((ErrorCode.NOT_FOUND_HRPERFOINQUIRY)));
    }

    // 작성자가 같은지 체크하기
    private void checkEmpEquals(String empId, String targetEmpId) {
        if (!empId.equals(targetEmpId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER); // 사용자 정의 예외
        }
    }

    // 받아온 EMP를 이용해 최종 점수 계산하기
    private Double getfinalScore(Employee emp) {

        // 해당 사원의 KPI 불러오기
        List<Kpi> kpiList = kpiCommandRepository.findByEmpId(emp.getEmpId());

        // 해당 사원의 평가 결과 불러오기
        List<HrPerfoHistory> hrPerfoHistoryList = hrPerfoHistoryRepository.findByEmpId(emp.getEmpId());

        // 부서 가중치 가져오기
        Weight perfoWeight = weightCommandRepository.findWeightByDeptId(emp.getDept().getDepartmentId());

        // 개인 KPI 달성률 평균 생성
        Double personalKpiAverageScore = personalKpiAverage(kpiList);

        // 팀 KPI 달성률 평균 생성
        Double teamKpiAverageScore = teamKpiAverage(kpiList);

        // 동료 평가 점수 추출
        Double colPerfoScore = colPerfo(hrPerfoHistoryList);

        // 하향 평가 점수 추출
        Double downPerfoScore = downPerfo(hrPerfoHistoryList);

        // 근태 점수 추출 기능 추가 예정
        Double workAttitude = 93.5;

        // 최종 평가 점수 추출
        Double finalScore = calFinalScore(
                personalKpiAverageScore,
                teamKpiAverageScore,
                colPerfoScore,
                downPerfoScore,
                workAttitude,
                perfoWeight);

        return finalScore;
    }

    // 개인 KPI 평균치 생성
    private Double personalKpiAverage(List<Kpi> kpiList) {

        // 데이터 존재 여부 체크
        if (kpiList == null || kpiList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_KPI); // 리스트 자체가 null이거나 비어 있는 경우 예외 발생
        }

        // 현재 연도 가져오기
        int currentYear = java.time.Year.now().getValue();

        // personalType이 "personal"인 currentValue 값만 합산 후 평균 계산
        return kpiList.stream()
                .filter(kpi -> "PERSONAL".equalsIgnoreCase(String.valueOf(kpi.getPersonalType()))) // personalType 조건
                .filter(kpi -> kpi.getCreateDatetime() != null && // null 체크
                        kpi.getCreateDatetime().getYear() == currentYear) // 연도 조건
                .mapToDouble(kpi -> Math.min(kpi.getGoalValue() / kpi.getCurrentValue() * 100, 200)) // 값을 추출하며 200을 초과하는 값을 200으로 치환
                .average() // 평균 계산
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_KPI)); // 값이 없으면 예외발생
    }

    // 팀 KPI 평균치 생성
    private Double teamKpiAverage(List<Kpi> kpiList) {
        // 데이터 존재 여부 체크
        if (kpiList == null || kpiList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_KPI); // 리스트 자체가 null이거나 비어 있는 경우 예외 발생
        }

        // 현재 연도 가져오기
        int currentYear = java.time.Year.now().getValue();

        // personalType이 "personal"인 currentValue 값만 합산 후 평균 계산
        return kpiList.stream()
                .filter(kpi -> "TEAM".equalsIgnoreCase(String.valueOf(kpi.getPersonalType()))) // personalType 조건
                .filter(kpi -> kpi.getCreateDatetime() != null && // null 체크
                        kpi.getCreateDatetime().getYear() == currentYear) // 연도 조건
                .mapToDouble(kpi -> Math.min(kpi.getGoalValue() / kpi.getCurrentValue() * 100, 200)) // 값을 추출하며 200을 초과하는 값을 200으로 치환
                .average() // 평균 계산
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_KPI)); // 값이 없으면 예외발생
    }

    // 동료 평가 점수 꺼내기
    private Double colPerfo(List<HrPerfoHistory> hrPerfoHistoryList) {

        // 데이터 존재 여부 체크
        if (hrPerfoHistoryList == null || hrPerfoHistoryList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_HRPERFO);
        }

        // 현재 연도 가져오기
        int currentYear = java.time.Year.now().getValue();

        // 년도와 차수, 평가 종류 체크 후 점수 반환
        return Double.valueOf(hrPerfoHistoryList.stream()
                .filter(hrPerfo -> hrPerfo.getCreateDatetime() != null &&
                        hrPerfo.getCreateDatetime().getYear() == currentYear) // 올해 데이터
                .max(Comparator.comparingLong(HrPerfoHistory::getAdjustmentDegree)) // 가장 높은 차수 찾기
                .map(HrPerfoHistory::getAdjustmentColScore) // 점수 추출
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HRPERFO))); // 조건에 맞는 데이터 없으면 예외
    }


    // 하향 평가 점수 꺼내기
    private Double downPerfo(List<HrPerfoHistory> hrPerfoHistoryList) {

        // 데이터 존재 여부 체크
        if (hrPerfoHistoryList == null || hrPerfoHistoryList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_HRPERFO);
        }

        // 현재 연도 가져오기
        int currentYear = java.time.Year.now().getValue();

        // 년도와 차수, 평가 종류 체크 후 점수 반환
        return Double.valueOf(hrPerfoHistoryList.stream()
                .filter(hrPerfo -> hrPerfo.getCreateDatetime() != null &&
                        hrPerfo.getCreateDatetime().getYear() == currentYear) // 올해 데이터
                .max(Comparator.comparingLong(HrPerfoHistory::getAdjustmentDegree)) // 가장 높은 차수 찾기
                .map(HrPerfoHistory::getAdjustmentDownScore) // 점수 추출
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HRPERFO))); // 조건에 맞는 데이터 없으면 예외
    }

    // 최종 평가 점수 추출
    private Double calFinalScore(Double personalKpiAverageScore,
                                 Double teamKpiAverageScore,
                                 Double colPerfoScore,
                                 Double downPerfoScore,
                                 Double workAttitude,
                                 Weight perfoWeight) {

        // kpi 만점
        final Double kpiMax = 200.0;

        // 평가 만점
        final Double perfoMax= 25.0;

        // 근태 만점
        final Double attitudeMax = 100.0;

        // 가중치 계산된 개인 KPI 점수
        // kpi 만점인 200을 기준으로 나눈 후 가중치 만큼 곱하여 가중치 비율에 따른 점수 책정
        // 소수점 2자리까지 계산
        Double finalPersonalKPIScore = Math.round((personalKpiAverageScore / kpiMax * perfoWeight.getPersonalWeight()) * 100) / 100.0;

        // 가중치 계산된 팀 KPI 점수
        // kpi 만점인 200을 기준으로 나눈 후 가중치 만큼 곱하여 가중치 비율에 따른 점수 책정
        // 소수점 2자리까지 계산
        Double finalTeamKPIScore = Math.round((teamKpiAverageScore / kpiMax * perfoWeight.getTeamWeight()) * 100) / 100.0;

        // 가중치 계산된 동료 평가 점수
        // 평가 만점인 25을 기준으로 나눈 후 가중치 만큼 곱하여 가중치 비율에 따른 점수 책점
        // 소수점 2자리까지 계산
        Double finalColPerfoScore = Math.round((colPerfoScore / perfoMax * perfoWeight.getColWeight()) * 100) / 100.0;

        // 가중치 계산된 하향 평가 점수
        // 평가 만점인 25을 기준으로 나눈 후 가중치 만큼 곱하여 가중치 비율에 따른 점수 책점
        // 소수점 2자리까지 계산
        Double finalDownPerfoScore = Math.round((downPerfoScore / perfoMax * perfoWeight.getDownwardWeight()) * 100) / 100.0;

        // 가중치 계산된 근태 점수
        // 평가 만점인 100을 기준으로 나눈 후 가중치 만큼 곱하여 가중치 비율에 따른 점수 책정
        // 소수점 2자리까지 계산
        Double finalWorkScore =  Math.round((workAttitude / attitudeMax * perfoWeight.getAttendanceWeight()) * 100) / 100.0;

        // 총점 추출
        Double finalPerfoScore = finalPersonalKPIScore + finalTeamKPIScore + finalColPerfoScore + finalDownPerfoScore + finalWorkScore;

        return BigDecimal.valueOf(finalPerfoScore).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
