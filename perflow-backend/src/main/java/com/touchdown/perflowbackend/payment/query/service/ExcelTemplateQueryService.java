package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeOvertimeQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelTemplateQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;
    private final WorkAttitudeOvertimeQueryService workAttitudeOvertimeQueryService;

    // 급여대장 엑셀 템플릿 생성 메서드
    public byte[] generatePayrollTemplate() throws IOException {
        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payroll");

        // 헤더 작성
        createHeader(sheet);

        // 사원 정보 가져오기
        List<Employee> employees = employeeQueryRepository.findActiveEmployees();

        // 초과근무 정보 가져오기
        List<WorkAttitudeOvertimeForTeamLeaderSummaryDTO> overtimeSummaries = workAttitudeOvertimeQueryService.getOvertimeSummaryForAllEmployees();

        // overtimeSummaryMap 초기화
        Map<String, WorkAttitudeOvertimeForTeamLeaderSummaryDTO> overtimeSummaryMap = overtimeSummaries.stream()
                .collect(Collectors.toMap(WorkAttitudeOvertimeForTeamLeaderSummaryDTO::getEmpId, summary -> summary));


        // 데이터 행 작성
        int rowIndex = 1;
        for (Employee employee : employees) {

            createEmployeeRow(sheet, rowIndex++, employee, overtimeSummaryMap); // overtimeSummaryMap 전달

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
    private void createEmployeeRow(Sheet sheet, int rowIndex, Employee employee, Map<String, WorkAttitudeOvertimeForTeamLeaderSummaryDTO> overtimeSummaryMap) {

        Row row = sheet.createRow(rowIndex);

        // 숫자 서식 지정 (천 단위 쉼표)
        CellStyle numberCellStyle = sheet.getWorkbook().createCellStyle();
        DataFormat format = sheet.getWorkbook().createDataFormat();
        numberCellStyle.setDataFormat(format.getFormat("#,##0"));

        row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
        row.createCell(1).setCellValue(employee.getName()); // 사원 이름
        row.createCell(2).setCellValue(employee.getPosition().getName()); // 직위 이름
        row.createCell(3).setCellValue(employee.getDept().getName()); // 부서 이름

        String empStatus = employee.getStatus().name();
        String statusInKorean = "";

        if ("ACTIVE".equals(empStatus)) {

            statusInKorean = "재직"; // ACTIVE일 경우 '재직'

        }

        row.createCell(4).setCellValue(statusInKorean); // 재직상태
        Cell basicPayCell = row.createCell(5);
        basicPayCell.setCellValue(employee.getPay()); // 기본급
        basicPayCell.setCellStyle(numberCellStyle);

        // 기본급을 시간당 통산 임금으로 변환
        Long hourlyPay = employee.getPay() / 209;

        // Overtime 계산된 값 가져오기
        WorkAttitudeOvertimeForTeamLeaderSummaryDTO summary = overtimeSummaryMap.get(employee.getEmpId());
        long extendedAllowance = summary != null ? calculateAllowance(hourlyPay, summary.getExtendedHours(), 1.5) : 0;
        long nightAllowance = summary != null ? calculateAllowance(hourlyPay, summary.getNightHours(), 1.5) : 0;
        long holidayAllowance = summary != null ? calculateAllowance(hourlyPay, summary.getHolidayHours(), summary.getHolidayHours() > 8 ? 2.0 : 1.5) : 0;

        // 연장근무수당
        Cell extendedAllowanceCell = row.createCell(6);
        extendedAllowanceCell.setCellValue(extendedAllowance);
        extendedAllowanceCell.setCellStyle(numberCellStyle);

        // 야간근무수당
        Cell nightAllowanceCell = row.createCell(7);
        nightAllowanceCell.setCellValue(nightAllowance);
        nightAllowanceCell.setCellStyle(numberCellStyle);

        // 휴일근무수당
        Cell holidayAllowanceCell = row.createCell(8);
        holidayAllowanceCell.setCellValue(holidayAllowance);
        holidayAllowanceCell.setCellStyle(numberCellStyle);

        // 연차수당 (12월인 경우에만 계산)
        Long annualAllowance = 0L;
        if (LocalDate.now().getMonthValue() == 12) {
            annualAllowance = calculateAnnualAllowance(hourlyPay);
        }
        Cell annualAllowanceCell = row.createCell(9);
        annualAllowanceCell.setCellValue(annualAllowance);
        annualAllowanceCell.setCellStyle(numberCellStyle);

        // 성과급
        long incentive = 0L;
        Cell incentiveCell = row.createCell(10);
        incentiveCell.setCellValue(incentive);
        incentiveCell.setCellStyle(numberCellStyle);

        // 지급 내역 합계
        long totalPayment = employee.getPay() + extendedAllowance + nightAllowance + holidayAllowance + annualAllowance + incentive;
        Cell totalPaymentCell = row.createCell(11);
        totalPaymentCell.setCellValue(totalPayment);
        totalPaymentCell.setCellStyle(numberCellStyle);

        // 국민 연금
        long nationalPension = Math.round(employee.getPay() * 0.045);
        Cell nationalPensionCell = row.createCell(12);
        nationalPensionCell.setCellValue(nationalPension);
        nationalPensionCell.setCellStyle(numberCellStyle);

        // 건강 보험
        long healthInsurance = Math.round(employee.getPay() * 0.03545);
        Cell healthInsuranceCell = row.createCell(13);
        healthInsuranceCell.setCellValue(healthInsurance);
        healthInsuranceCell.setCellStyle(numberCellStyle);

        // 고용 보험
        long hireInsurance = Math.round(employee.getPay() * 0.004591);
        Cell hireInsuranceCell = row.createCell(14);
        hireInsuranceCell.setCellValue(hireInsurance);
        hireInsuranceCell.setCellStyle(numberCellStyle);

        // 장기 요양 보험
        long longTermCareInsurance = Math.round(employee.getPay() * 0.009);
        Cell longTermCareInsuranceCell = row.createCell(15);
        longTermCareInsuranceCell.setCellValue(longTermCareInsurance);
        longTermCareInsuranceCell.setCellStyle(numberCellStyle);

        // 소득세
        long annualPay = employee.getPay() * 12;
        long incomeTax = calculateIncomeTax(annualPay) / 12;
        Cell incomeTaxCell = row.createCell(16);
        incomeTaxCell.setCellValue(incomeTax);
        incomeTaxCell.setCellStyle(numberCellStyle);

        // 지방소득세
        long localIncomeTax = Math.round(incomeTax * 0.1);
        localIncomeTax = (localIncomeTax / 10) * 10; // 일의 자리 수를 0으로 처리
        Cell localIncomeTaxCell = row.createCell(17);
        localIncomeTaxCell.setCellValue(localIncomeTax);
        localIncomeTaxCell.setCellStyle(numberCellStyle);

        // 공제 내역 합계
        long totalDeduction = nationalPension + healthInsurance + hireInsurance + longTermCareInsurance + incomeTax + localIncomeTax;
        Cell totalDeductionCell = row.createCell(18);
        totalDeductionCell.setCellValue(totalDeduction);
        totalDeductionCell.setCellStyle(numberCellStyle);

        // 총합계
        long totalAmount = totalPayment - totalDeduction;
        Cell totalAmountCell = row.createCell(19);
        totalAmountCell.setCellValue(totalAmount);
        totalAmountCell.setCellStyle(numberCellStyle);

        row.createCell(20).setCellValue("PENDING"); // 지급 상태(대기)

    }

    private Long calculateAllowance(Long hourlyPay, long hours, double rateMultiplier) {

        double allowance = hourlyPay * hours * rateMultiplier;
        return Math.round(allowance);

    }

    private Long calculateAnnualAllowance(Long hourlyPay) {

        return (long) Math.round(hourlyPay * 8);

    }

    private long calculateIncomeTax(long annualPay) {

        if (annualPay <= 30000000) {

            return Math.round(3100000 + annualPay * 0.04);

        } else if (annualPay <= 45000000) {

            return Math.round(3100000 + annualPay * 0.04 - (annualPay - 30000000) * 0.05);

        } else if (annualPay <= 70000000) {

            return Math.round(3100000 + annualPay * 0.015);

        } else if (annualPay <= 120000000) {

            return Math.round(3100000 + annualPay * 0.005);

        } else {

            return 0; // 1억 2천만원 초과 시 소득세가 0

        }
    }

}