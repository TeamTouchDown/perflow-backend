package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelTemplateQueryService {

    private final EmployeeCommandRepository employeeCommandRepository; // 사원 정보를 가져오는 레포지토리

    public byte[] generatePayrollTemplate() throws IOException {
        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payroll Template");

        // 헤더 행 생성
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

        // 사원 정보 가져오기
        List<Employee> employees = employeeCommandRepository.findAll();

        int rowIndex = 1;
        for (Employee employee : employees) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
            row.createCell(1).setCellValue(employee.getName()); // 사원 이름
            row.createCell(2).setCellValue(employee.getPosition().getName()); // 직위 이름
            row.createCell(3).setCellValue(employee.getDept().getName()); // 부서 이름
            row.createCell(4).setCellValue(employee.getStatus().name()); // 재직상태
            row.createCell(5).setCellValue(employee.getPay()); // 기본급
            row.createCell(6).setCellValue(0); // 연장근무수당
            row.createCell(7).setCellValue(0); // 야간근무수당
            row.createCell(8).setCellValue(0); // 휴일근무수당
            row.createCell(9).setCellValue(0); // 연차수당
            row.createCell(10).setCellValue(0); // 성과급
            row.createCell(11).setCellValue(0); // 지급 내역 합계
            row.createCell(12).setCellValue(0); // 국민연금
            row.createCell(13).setCellValue(0); // 건강보험
            row.createCell(14).setCellValue(0); // 고용보험
            row.createCell(15).setCellValue(0); // 장기요양보험
            row.createCell(16).setCellValue(0); // 소득세
            row.createCell(17).setCellValue(0); // 지방소득세
            row.createCell(18).setCellValue(0); // 공제 내역 합계
            row.createCell(19).setCellValue(0); // 총합계
            row.createCell(20).setCellValue("PENDING"); // 지급 상태(대기)

        }

        // 템플릿을 ByteArray로 변환하여 반환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();

    }
}
