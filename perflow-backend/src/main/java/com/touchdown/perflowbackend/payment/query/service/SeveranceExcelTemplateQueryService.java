package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
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
public class SeveranceExcelTemplateQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;

    @Transactional
    public byte[] generateSeverancePayTemplate() throws IOException {

        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SeverancePay Template");

        // 헤더 작성
        createHeader(sheet);

        // 사원 정보 가져오기
        List<Employee> employees = employeeQueryRepository.findAll();

        // 데이터 행 작성
        int rowIndex = 1;
        for (Employee employee : employees) {

            createEmployeeRow(sheet, rowIndex++, employee);

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
    private void createEmployeeRow(Sheet sheet, int rowIndex, Employee employee) {

        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
        row.createCell(1).setCellValue(employee.getName()); // 사원 이름
        row.createCell(2).setCellValue(employee.getJoinDate()); // 입사일
        row.createCell(3).setCellValue(employee.getResignDate()); // 퇴사일
        row.createCell(4).setCellValue(employee.getPosition().getName()); // 직위 이름
        row.createCell(5).setCellValue(employee.getDept().getName()); // 부서 이름
        row.createCell(6).setCellValue(employee.getPay() * 3); // 3개월 간 기본급
        row.createCell(7).setCellValue(0); // 연장근무수당
        row.createCell(8).setCellValue(0); // 야간근무수당
        row.createCell(9).setCellValue(0); // 휴일근무수당
        row.createCell(10).setCellValue(0); // 연차수당
        row.createCell(11).setCellValue(0); // 총합계
        row.createCell(12).setCellValue("PENDING"); // 지급 상태(대기)

    }

}
