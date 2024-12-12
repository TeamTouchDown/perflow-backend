package com.touchdown.perflowbackend.payment.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePayDetail;
import com.touchdown.perflowbackend.payment.command.domain.repository.SeverancePayCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeveranceExcelTemplateCommandService {

    private final SeverancePayCommandRepository severancePayCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;

    public void uploadSeveranceTemplate(MultipartFile file, String originalFileName) throws IOException {

        try (InputStream inputStream = file.getInputStream()) {

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다.

            // 파일 이름을 사용해 SeverancePay 객체 생성
            SeverancePay severancePay = new SeverancePay(originalFileName);

            // 엑셀 데이터 파싱 시작 (첫 번째 행은 헤더이므로 두 번째 행부터 시작)
            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // 첫 번째 행은 헤더, 건너뜀

                // 사원 ID 가져오기
                String empId = row.getCell(0).getStringCellValue(); // 사원 ID (문자열)

                Employee employee = employeeCommandRepository.findById(empId)
                        .orElseThrow(() -> {

                            log.error("Employee with id {} not found", empId);

                            return new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE);

                        });

                // SeverancePayDetail 객체 생성
                SeverancePayDetail severancePayDetail = new SeverancePayDetail(

                        employee,
                        (long) row.getCell(7).getNumericCellValue(), // 연장근무수당
                        (long) row.getCell(8).getNumericCellValue(), // 야간근무수당
                        (long) row.getCell(9).getNumericCellValue(), // 휴일근무수당
                        (long) row.getCell(10).getNumericCellValue(), // 연차수당
                        (long) row.getCell(11).getNumericCellValue() // 총합계

                );

                // SeverancePay 객체에 SeverancePayDetail 추가
                severancePay.addSeverancePayDetail(severancePayDetail);

            }

            // SeverancePay 객체를 DB에 저장 (하나의 severancePayId로 여러 급여 항목을 저장)
            severancePayCommandRepository.save(severancePay);

        }
    }
}
