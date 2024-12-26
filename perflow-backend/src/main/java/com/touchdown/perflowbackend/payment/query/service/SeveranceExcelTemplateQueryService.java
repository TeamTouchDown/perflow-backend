package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.dto.ThreeMonthOvertimeDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeOvertimeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeveranceExcelTemplateQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;
    private final WorkAttitudeOvertimeQueryRepository workAttitudeOvertimeQueryRepository;

    @Transactional
    public byte[] generateSeverancePayTemplate() throws IOException {

        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SeverancePay");

        // 헤더 작성
        createHeader(sheet);

        // 사원 정보 가져오기
        List<Employee> employees = employeeQueryRepository.findResignedEmployees();

        // 데이터 행 작성
        int rowIndex = 1;
        for (Employee employee : employees) {

            // 퇴사일 3개월 전 날짜 계산
            LocalDate threeMonthsAgo = employee.getResignDate().minusMonths(3);

            // 퇴사일 3개월 전까지 연장근무 시간 등을 가져오는 쿼리 호출
            List<ThreeMonthOvertimeDTO> overtimeSummaryList = workAttitudeOvertimeQueryRepository.findOvertimeSummaryForResignedEmployees(threeMonthsAgo);

            // overtimeSummaryList를 empId를 키로 하는 맵으로 변환
            Map<String, ThreeMonthOvertimeDTO> overtimeSummaryMap = overtimeSummaryList.stream()
                    .collect(Collectors.toMap(ThreeMonthOvertimeDTO::getEmpId, Function.identity()));

            // 사원에 해당하는 초과근무 데이터를 맵에서 조회
            ThreeMonthOvertimeDTO overtimeSummary = overtimeSummaryMap.get(employee.getEmpId());

            createEmployeeRow(sheet, rowIndex++, employee, overtimeSummary);

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
        header.createCell(2).setCellValue("입사일");
        header.createCell(3).setCellValue("퇴사일");
        header.createCell(4).setCellValue("직위");
        header.createCell(5).setCellValue("부서");
        header.createCell(6).setCellValue("기본급");
        header.createCell(7).setCellValue("연장근무수당");
        header.createCell(8).setCellValue("야간근무수당");
        header.createCell(9).setCellValue("휴일근무수당");
        header.createCell(10).setCellValue("연차수당");
        header.createCell(11).setCellValue("총합계");
        header.createCell(12).setCellValue("지급상태");

    }

    // 사원 데이터를 행으로 추가하는 메서드
    private void createEmployeeRow(Sheet sheet, int rowIndex, Employee employee, ThreeMonthOvertimeDTO overtimeSummary) {

        Row row = sheet.createRow(rowIndex);

        // 숫자 서식 지정 (천 단위 쉼표)
        CellStyle numberCellStyle = sheet.getWorkbook().createCellStyle();
        DataFormat format = sheet.getWorkbook().createDataFormat();
        numberCellStyle.setDataFormat(format.getFormat("#,##0"));

        // 날짜 셀 스타일 생성
        Workbook workbook = sheet.getWorkbook();
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
        row.createCell(1).setCellValue(employee.getName()); // 사원 이름

        // 입사일 설정
        Cell joinDateCell = row.createCell(2);
        if (employee.getJoinDate() != null) {
            joinDateCell.setCellValue(employee.getJoinDate());
            joinDateCell.setCellStyle(dateCellStyle);
        }

        // 퇴사일 설정
        Cell resignDateCell = row.createCell(3);
        if (employee.getResignDate() != null) {
            resignDateCell.setCellValue(employee.getResignDate());
            resignDateCell.setCellStyle(dateCellStyle);
        }

        row.createCell(4).setCellValue(employee.getPosition().getName()); // 직위 이름
        row.createCell(5).setCellValue(employee.getDept().getName()); // 부서 이름

        // 3개월 간 기본급
        long threeMonthPay = employee.getPay() * 3;
        Cell threeMonthPayCell = row.createCell(6);
        threeMonthPayCell.setCellValue(threeMonthPay);
        threeMonthPayCell.setCellStyle(numberCellStyle);

        long hourlyPay = employee.getPay() / 209;

        long extendedAllowance = overtimeSummary != null ? calculateThreeMonthAllowance(hourlyPay, overtimeSummary.getExtendedHours(), 1.5) : 0;
        long nightAllowance = overtimeSummary != null ? calculateThreeMonthAllowance(hourlyPay, overtimeSummary.getNightHours(), 1.5) : 0;
        long holidayAllowance = overtimeSummary != null ? calculateThreeMonthAllowance(hourlyPay, overtimeSummary.getHolidayHours(), overtimeSummary.getHolidayHours() > 8 ? 2.0 : 1.5) : 0;

        // 연장근무수당
        Cell extendedAllowanceCell = row.createCell(7);
        extendedAllowanceCell.setCellValue(extendedAllowance);
        extendedAllowanceCell.setCellStyle(numberCellStyle);

        // 야간근무수당
        Cell nightAllowanceCell = row.createCell(8);
        nightAllowanceCell.setCellValue(nightAllowance);
        nightAllowanceCell.setCellStyle(numberCellStyle);

        // 휴일근무수당
        Cell holidayAllowanceCell = row.createCell(9);
        holidayAllowanceCell.setCellValue(holidayAllowance);
        holidayAllowanceCell.setCellStyle(numberCellStyle);

        // 연차수당 (12월인 경우에만 계산)
        Long annualAllowance = 0L;
        if (LocalDate.now().getMonthValue() == 12) {
            annualAllowance = calculateResignedAnnualAllowance(hourlyPay);
        }
        Cell annualAllowanceCell = row.createCell(10);
        annualAllowanceCell.setCellValue(annualAllowance);
        annualAllowanceCell.setCellStyle(numberCellStyle);

        // 총 합계
        long totalAmount = employee.getPay() * 3 + extendedAllowance + nightAllowance + holidayAllowance + annualAllowance;
        Cell totalAmountCell = row.createCell(11);
        totalAmountCell.setCellValue(totalAmount);
        totalAmountCell.setCellStyle(numberCellStyle);

        row.createCell(12).setCellValue("PENDING"); // 지급 상태(대기)

    }

    private long calculateThreeMonthAllowance(long hourlyPay, long hours, double rateMultiplier) {

        double allowance = hourlyPay * hours * rateMultiplier;
        return Math.round(allowance);

    }

    private Long calculateResignedAnnualAllowance(long hourlyPay) {

        return (long) Math.round(hourlyPay * 8);

    }

}
