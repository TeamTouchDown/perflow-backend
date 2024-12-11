package com.touchdown.perflowbackend.payment.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.PayrollDetail;
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

@Service
@RequiredArgsConstructor
public class PayrollCommandService {

    private final PayrollCommandRepository payrollCommandRepository;

    @Transactional
    public void updatePayrollFromExcel(
            Long payrollId, MultipartFile file
    ) throws IOException {

        try (InputStream inputStream = file.getInputStream()) {
            // 엑셀 파일을 Workbook 객체로 변환
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다.

            // 1. 급여대장 정보 조회
            Payroll payroll = payrollCommandRepository.findByPayrollIdWithDetails(payrollId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAYROLL));

            // 2. 엑셀 데이터 파싱 시작
            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // 첫 번째 행은 헤더, 건너뜀

                // 사원 ID와 급여 정보를 읽어옴
                String empId = row.getCell(0).getStringCellValue(); // 사원 ID (문자열)
                PayrollDetail payrollDetail = payroll.getPayrollDetailList().stream()
                        .filter(detail -> detail.getEmp().getEmpId().equals(empId))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

                // 급여 항목들 읽어오기
                Long extendLaborAllowance = (long) row.getCell(6).getNumericCellValue();
                Long nightLaborAllowance = (long) row.getCell(7).getNumericCellValue();
                Long holidayLaborAllowance = (long) row.getCell(8).getNumericCellValue();
                Long annualAllowance = (long) row.getCell(9).getNumericCellValue();
                Long incentive = (long) row.getCell(10).getNumericCellValue();
                Long nationalPension = (long) row.getCell(12).getNumericCellValue();
                Long healthInsurance = (long) row.getCell(13).getNumericCellValue();
                Long hireInsurance = (long) row.getCell(14).getNumericCellValue();
                Long longTermCareInsurance = (long) row.getCell(15).getNumericCellValue();
                Long incomeTax = (long) row.getCell(16).getNumericCellValue();
                Long localIncomeTax = (long) row.getCell(17).getNumericCellValue();
                Long totalAmount = (long) row.getCell(19).getNumericCellValue();

                // 급여 정보를 수정
                payrollDetail.updatePayroll(

                        extendLaborAllowance, nightLaborAllowance, holidayLaborAllowance,
                        annualAllowance, incentive, nationalPension, healthInsurance, hireInsurance,
                        longTermCareInsurance, incomeTax, localIncomeTax, totalAmount);

            }

            // 3. 급여대장 정보 저장
            payrollCommandRepository.save(payroll);

        }
    }
}

