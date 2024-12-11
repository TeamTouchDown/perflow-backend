package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.PayrollDetail;
import com.touchdown.perflowbackend.payment.query.dto.*;
import com.touchdown.perflowbackend.payment.query.repository.PayrollQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollQueryService {

    private final PayrollQueryRepository payrollQueryRepository;

    // 급여 데이터에 대한 엑셀 생성 메서드
    @Transactional
    public byte[] generatePayroll(Long payrollId) throws IOException {

        // 데이터베이스에서 급여 데이터를 조회
        Payroll payroll = payrollQueryRepository.findByPayrollsId(payrollId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAYROLL));

        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payroll");

        // 헤더 작성
        createHeader(sheet);

        // 급여 상세 리스트를 가져와서 사원 정보와 급여 정보 작성
        List<PayrollDetail> payrollDetails = payroll.getPayrollDetailList(); // PayrollDetail 리스트 가져오기

        // 데이터 행 작성
        int rowIndex = 1;
        for (PayrollDetail payrollDetail : payrollDetails) {

            Employee employee = payrollDetail.getEmp(); // PayrollDetail에서 사원 정보 가져오기
            createEmployeeRow(sheet, rowIndex++, employee, payrollDetail); // 사원과 급여 정보 작성

        }

        // 템플릿을 ByteArray로 변환하여 반환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    // 헤더를 작성하는 메서드
    private void createHeader(Sheet sheet) {

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("사번");
        header.createCell(1).setCellValue("이름");
        header.createCell(2).setCellValue("직위");
        header.createCell(3).setCellValue("부서");
        header.createCell(4).setCellValue("재직상태");
        header.createCell(5).setCellValue("기본급");
        header.createCell(6).setCellValue("연장근무수당");
        header.createCell(7).setCellValue("야간근무수당");
        header.createCell(8).setCellValue("휴일근무수당");
        header.createCell(9).setCellValue("연차수당");
        header.createCell(10).setCellValue("성과급");
        header.createCell(11).setCellValue("지급내역합계");
        header.createCell(12).setCellValue("국민연금");
        header.createCell(13).setCellValue("건강보험");
        header.createCell(14).setCellValue("고용보험");
        header.createCell(15).setCellValue("장기요양보험");
        header.createCell(16).setCellValue("소득세");
        header.createCell(17).setCellValue("지방소득세");
        header.createCell(18).setCellValue("공제내역합계");
        header.createCell(19).setCellValue("총합계");
        header.createCell(20).setCellValue("지급상태");

    }

    // 사원 데이터를 행으로 추가하는 메서드
    private void createEmployeeRow(Sheet sheet, int rowIndex, Employee employee, PayrollDetail payrollDetail) {

        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
        row.createCell(1).setCellValue(employee.getName()); // 사원 이름
        row.createCell(2).setCellValue(employee.getPosition().getName()); // 직위 이름
        row.createCell(3).setCellValue(employee.getDept().getName()); // 부서 이름
        row.createCell(4).setCellValue(employee.getStatus().name()); // 재직상태
        row.createCell(5).setCellValue(employee.getPay()); // 기본급
        row.createCell(6).setCellValue(payrollDetail.getExtendLaborAllowance()); // 연장근무수당
        row.createCell(7).setCellValue(payrollDetail.getNightLaborAllowance()); // 야간근무수당
        row.createCell(8).setCellValue(payrollDetail.getHolidayLaborAllowance()); // 휴일근무수당
        row.createCell(9).setCellValue(payrollDetail.getAnnualAllowance()); // 연차수당
        row.createCell(10).setCellValue(payrollDetail.getIncentive()); // 성과급
        row.createCell(11).setCellValue(
                        employee.getPay() +
                        payrollDetail.getExtendLaborAllowance() +
                        payrollDetail.getNightLaborAllowance() +
                        payrollDetail.getHolidayLaborAllowance() +
                        payrollDetail.getAnnualAllowance() +
                        payrollDetail.getIncentive()
        ); // 지급 내역 합계
        row.createCell(12).setCellValue(payrollDetail.getNationalPension()); // 국민연금
        row.createCell(13).setCellValue(payrollDetail.getHealthInsurance()); // 건강보험
        row.createCell(14).setCellValue(payrollDetail.getHireInsurance()); // 고용보험
        row.createCell(15).setCellValue(payrollDetail.getLongTermCareInsurance()); // 장기요양보험
        row.createCell(16).setCellValue(payrollDetail.getIncomeTax()); // 소득세
        row.createCell(17).setCellValue(payrollDetail.getLocalIncomeTax()); // 지방소득세
        row.createCell(18).setCellValue(
                        payrollDetail.getNationalPension() +
                        payrollDetail.getHealthInsurance() +
                        payrollDetail.getHireInsurance() +
                        payrollDetail.getLongTermCareInsurance() +
                        payrollDetail.getIncomeTax() +
                        payrollDetail.getLocalIncomeTax()
        ); // 공제 내역 합계
        row.createCell(19).setCellValue(payrollDetail.getTotalAmount()); // 총합계
        row.createCell(20).setCellValue(payrollDetail.getStatus().name()); // 지급 상태

    }

    @Transactional(readOnly = true)
    public PayrollListResponseDTO getPayrolls(Pageable pageable) {

        Page<Payroll> page = payrollQueryRepository.findAll(pageable);

        List<PayrollResponseDTO> payrolls = page.getContent().stream()
                .map(payroll -> {
                    // 총 사원 수 (payrollDetailList의 크기)
                    long totalEmp = payroll.getPayrollDetailList().size();

                    // 총 지급 금액 (payrollDetailList의 totalAmount 합산)
                    long totalPay = payroll.getPayrollDetailList().stream()
                            .mapToLong(PayrollDetail::getTotalAmount) // PayrollDetail의 totalAmount 값을 합산
                            .sum();

                    return PayrollResponseDTO.builder()
                            .payrollId(payroll.getPayrollId())
                            .name(payroll.getName())
                            .createDatetime(LocalDate.from(payroll.getCreateDatetime()))
                            .totalEmp(totalEmp)
                            .totalPay(totalPay)
                            .build();

                })
                .collect(Collectors.toList());

        return PayrollListResponseDTO.builder()
                .payrolls(payrolls)
                .totalPages(page.getTotalPages())
                .totalItems((int) page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();

    }

    @Transactional(readOnly = true)
    public PayrollDetailResponseDTO getPayroll(Long payrollId) {

        List<PayrollDTO> payroll = payrollQueryRepository.findByPayrollId(payrollId);

        return new PayrollDetailResponseDTO(payroll);

    }

    // 가장 최근 급여의 월을 기준으로 3년간 데이터를 조회하는 메서드
    @Transactional(readOnly = true)
    public List<PayrollChartDTO> getPayrollsByMonthAndThreeYears() {
        // 가장 최근 급여 데이터 조회
        PayrollChartDTO latestPayroll = payrollQueryRepository.findLatestPayroll();

        if (latestPayroll == null) {

            throw new CustomException(ErrorCode.NOT_FOUND_PAYROLL);

        }

        // 최근 급여 대장의 월과 연도를 기준으로 3년간 급여 데이터 조회
        int latestMonth = latestPayroll.getCreateDatetime().getMonthValue();
        int latestYear = latestPayroll.getCreateDatetime().getYear();
        int startYear = latestYear - 2;  // 3년 범위

        // 3년간 급여 데이터 조회
        return payrollQueryRepository.findPayrollsByMonthAndYears(latestMonth, startYear, latestYear);

    }

    // 가장 최근 급여의 월을 기준으로 3개월간 데이터를 조회하는 메서드
    @Transactional(readOnly = true)
    public List<PayrollChartDTO> getLastThreeMonthsPayrolls() {
        // 가장 최근 급여 데이터 조회
        PayrollChartDTO latestPayroll = payrollQueryRepository.findLatestPayroll();

        if (latestPayroll == null) {

            throw new CustomException(ErrorCode.NOT_FOUND_PAYROLL);

        }

        int latestMonth = latestPayroll.getCreateDatetime().getMonthValue();
        int latestYear = latestPayroll.getCreateDatetime().getYear();
        int startMonth = latestMonth - 2;  // 3개월 범위 (현재 월에서 2개월 전까지)

        if (startMonth <= 0) {

            startMonth += 12; // 12월에서 1월로 넘어갈 경우 처리
            latestYear -= 1;  // 작년으로 변경

        }

        // 3개월간 급여 데이터 조회
        return payrollQueryRepository.findPayrollsByMonths(startMonth, latestMonth, latestYear);

    }

    // 3년간 급여 데이터 조회
    @Transactional(readOnly = true)
    public List<PayrollChartDTO> getLastThreeYearsPayrolls() {
        // 가장 최근 급여 데이터 조회
        PayrollChartDTO latestPayroll = payrollQueryRepository.findLatestPayroll();

        if (latestPayroll == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PAYROLL);
        }

        int latestYear = latestPayroll.getCreateDatetime().getYear();
        int startYear = latestYear - 2; // 3년 범위

        // 3년간 급여 데이터 조회
        return payrollQueryRepository.findPayrollsByYears(startYear, latestYear);

    }

    @Transactional(readOnly = true)
    public PayStubDTO getPayStub(String empId) {

        PayrollDTO payStub = payrollQueryRepository.findByEmpId(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

        return new PayStubDTO(payStub);

    }
}