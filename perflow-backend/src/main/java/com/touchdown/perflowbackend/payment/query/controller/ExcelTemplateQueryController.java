package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class ExcelTemplateQueryController {

    private EmployeeCommandRepository employeeCommandRepository;  // 사원 정보를 가져오는 레포지토리

    @GetMapping("/payroll-template")
    public ResponseEntity<byte[]> downloadPayrollTemplate() throws IOException {

        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payroll Template");

        // 헤더 행 생성
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("사번"); // 직원 ID
        header.createCell(1).setCellValue("이름"); // 직원 이름
        header.createCell(2).setCellValue("직위"); // 직위
        header.createCell(3).setCellValue("부서"); // 부서
        header.createCell(4).setCellValue("기본급"); // 월급
        header.createCell(5).setCellValue("연장근무수당"); // 연장 근무 수당
        header.createCell(6).setCellValue("야간근무수당"); // 야간 근무 수당
        header.createCell(7).setCellValue("휴일근무수당"); // 휴일 근무 수당
        header.createCell(8).setCellValue("연차수당"); // 연차 수당
        header.createCell(9).setCellValue("지급내역합계"); // 지급 내역 합계
        header.createCell(10).setCellValue("국민연금"); // 국민연금
        header.createCell(11).setCellValue("건강보험"); // 건강보험
        header.createCell(12).setCellValue("고용보험"); // 고용보험
        header.createCell(13).setCellValue("장기요양보험"); // 장기요양보험
        header.createCell(14).setCellValue("소득세"); // 소득세
        header.createCell(15).setCellValue("지방소득세"); // 지방소득세
        header.createCell(16).setCellValue("공제내역합계"); // 공제 내역 합계
        header.createCell(17).setCellValue("총합계"); // 총 합계


        // 사원 정보 가져오기 (예시: 사원 목록을 가져옵니다)
        List<Employee> employees = employeeCommandRepository.findAll();

        int rowIndex = 1;
        for (Employee employee : employees) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(employee.getEmpId());  // 사원 ID
            row.createCell(1).setCellValue(employee.getName());  // 사원 이름
            String positionName = employee.getPosition().getName();  // 직위 이름을 가져옴
            row.createCell(2).setCellValue(positionName);  // 직위 이름
            String deptName = employee.getDept().getName();  // 부서 이름을 가져옴
            row.createCell(3).setCellValue(deptName); // 부서 이름
            row.createCell(4).setCellValue(employee.getPay()); // 월급
            row.createCell(5);  // 연장 근무 수당 (이후에 계산하여 값 설정)
            row.createCell(6);  // 야간 근무 수당 (이후에 계산하여 값 설정)
            row.createCell(7);  // 휴일 근무 수당 (이후에 계산하여 값 설정)
            row.createCell(8);  // 연차 수당 (이후에 계산하여 값 설정)
            row.createCell(9);  // 지급 내역 합계 (이후에 계산하여 값 설정)
            row.createCell(10); // 국민연금 (이후에 계산하여 값 설정)
            row.createCell(11); // 건강보험 (이후에 계산하여 값 설정)
            row.createCell(12); // 고용보험 (이후에 계산하여 값 설정)
            row.createCell(13); // 장기요양보험 (이후에 계산하여 값 설정)
            row.createCell(14); // 소득세 (이후에 계산하여 값 설정)
            row.createCell(15); // 지방소득세 (이후에 계산하여 값 설정)
            row.createCell(16); // 공제 내역 합계 (이후에 계산하여 값 설정)
            row.createCell(17); // 총 합계 (이후에 계산하여 값 설정)

        }

        // 템플릿을 ByteArray로 변환하여 반환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        // HTTP 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "payroll_template.xlsx");

        // 파일 데이터를 HTTP 응답으로 반환
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

    }
}