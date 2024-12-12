package com.touchdown.perflowbackend.payment.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePayDetail;
import com.touchdown.perflowbackend.payment.command.domain.repository.SeverancePayCommandRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class SeverancePayCommandService {

    private final SeverancePayCommandRepository severancePayCommandRepository;

    public void updateSeverancePayFromExcel(Long severancePayId, MultipartFile file) throws IOException {

        try (InputStream inputStream = file.getInputStream()) {
            // 엑셀 파일을 Workbook 객체로 변환
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다.

            // 1. 퇴직금 정보 조회
            SeverancePay severancePay = severancePayCommandRepository.findBySeverancePayIdWithDetails(severancePayId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEVERANCE_PAY));

            // 2. 엑셀 데이터 파싱 시작
            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // 첫 번째 행은 헤더, 건너뜀

                // 사원 ID와 퇴직금 정보를 읽어옴
                String empId = row.getCell(0).getStringCellValue(); // 사원 ID (문자열)
                SeverancePayDetail severancePayDetail = severancePay.getSeverancePayDetailList().stream()
                        .filter(detail -> detail.getEmp().getEmpId().equals(empId))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

                // 퇴직금 항목들 읽어오기
                Long extendLaborAllowance = (long) row.getCell(7).getNumericCellValue();
                Long nightLaborAllowance = (long) row.getCell(8).getNumericCellValue();
                Long holidayLaborAllowance = (long) row.getCell(9).getNumericCellValue();
                Long annualAllowance = (long) row.getCell(10).getNumericCellValue();
                Long totalAmount = (long) row.getCell(11).getNumericCellValue();

                // 퇴직금 정보를 수정
                severancePayDetail.updateSeverancePay(

                        extendLaborAllowance, nightLaborAllowance, holidayLaborAllowance,
                        annualAllowance, totalAmount);

            }

            // 3. 퇴직금 정보 저장
            severancePayCommandRepository.save(severancePay);

        }
    }
}
