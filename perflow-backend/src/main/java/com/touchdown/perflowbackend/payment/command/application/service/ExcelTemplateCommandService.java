package com.touchdown.perflowbackend.payment.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.command.domain.repository.PayrollCommandRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelTemplateCommandService {

    private final PayrollCommandRepository payrollCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;


    @Transactional
    public void uploadTemplate(MultipartFile file, String originalFileName) throws IOException {
        // 엑셀 파일을 InputStream으로 읽어오기
        try (InputStream inputStream = file.getInputStream()) {

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다.

            // 여러 사원의 급여대장 데이터를 저장할 리스트
            List<Payroll> payrollList = new ArrayList<>();

            // 엑셀 데이터 파싱 시작 (첫 번째 행은 헤더이므로 두 번째 행부터 시작)
            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // 첫 번째 행은 헤더, 건너뜀

                // 사원 ID 가져오기
                String empId = row.getCell(0).getStringCellValue(); // 사원 ID (문자열)
                Employee employee = employeeCommandRepository.findById(empId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

                // Payroll 객체 생성
                Payroll payroll = new Payroll(
                        employee,
                        (long) row.getCell(6).getNumericCellValue(), // 연장근무수당
                        (long) row.getCell(7).getNumericCellValue(), // 야간근무수당
                        (long) row.getCell(8).getNumericCellValue(), // 휴일근무수당
                        (long) row.getCell(9).getNumericCellValue(), // 연차수당
                        (long) row.getCell(10).getNumericCellValue(), // 성과급
                        (long) row.getCell(12).getNumericCellValue(), // 국민연금
                        (long) row.getCell(13).getNumericCellValue(), // 건강보험
                        (long) row.getCell(14).getNumericCellValue(), // 고용보험
                        (long) row.getCell(15).getNumericCellValue(), // 장기요양보험
                        (long) row.getCell(16).getNumericCellValue(), // 소득세
                        (long) row.getCell(17).getNumericCellValue(), // 지방소득세
                        (long) row.getCell(19).getNumericCellValue(), // 총합계
                        Status.PENDING // 기본 상태는 '대기'
                );

                // 리스트에 Payroll 객체 추가
                payrollList.add(payroll);

            }

            // 모든 Payroll 객체를 한 번에 DB에 저장
            payrollCommandRepository.saveAll(payrollList);

        }
    }
}
