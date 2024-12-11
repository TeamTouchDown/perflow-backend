package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.PayrollDetail;
import com.touchdown.perflowbackend.payment.query.repository.PayrollQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollQueryService {

    private final PayrollQueryRepository payrollQueryRepository;

    // 급여 데이터에 대한 엑셀 생성 메서드
    @Transactional
    public byte[] generatePayroll(Long payrollId) throws IOException {

        // 데이터베이스에서 급여 데이터를 조회
        Payroll payroll = payrollQueryRepository.findByPayrollId(payrollId)
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
}
