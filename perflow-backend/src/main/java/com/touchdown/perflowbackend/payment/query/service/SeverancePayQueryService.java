package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePayDetail;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayListResponseDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayResponseDTO;
import com.touchdown.perflowbackend.payment.query.repository.SeverancePayQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeverancePayQueryService {

    private final SeverancePayQueryRepository severancePayQueryRepository;

    @Transactional
    public byte[] generateSeverancePay(Long severancePayId) throws IOException {

        // 데이터베이스에서 급여 데이터를 조회
        SeverancePay severancePay = severancePayQueryRepository.findBySeverancePaysId(severancePayId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEVERANCE_PAY));

        // 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SeverancePay");

        // 헤더 작성
        createHeader(sheet);

        // 급여 상세 리스트를 가져와서 사원 정보와 급여 정보 작성
        List<SeverancePayDetail> severancePayDetails = severancePay.getSeverancePayDetailList(); // SeverancePayDetail 리스트 가져오기

        // 데이터 행 작성
        int rowIndex = 1;
        for (SeverancePayDetail severancePayDetail : severancePayDetails) {

            Employee employee = severancePayDetail.getEmp(); // severancePayDetail에서 사원 정보 가져오기
            createEmployeeRow(sheet, rowIndex++, employee, severancePayDetail); // 사원과 급여 정보 작성

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

    private void createEmployeeRow(Sheet sheet, int rowIndex, Employee employee, SeverancePayDetail severancePayDetail) {

        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue(employee.getEmpId()); // 사원 ID
        row.createCell(1).setCellValue(employee.getName()); // 사원 이름

        // 날짜 셀 스타일 생성
        Workbook workbook = sheet.getWorkbook();
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

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
        row.createCell(6).setCellValue(employee.getPay() * 3); // 기본급
        row.createCell(7).setCellValue(severancePayDetail.getExtendLaborAllowance()); // 연장근무수당
        row.createCell(8).setCellValue(severancePayDetail.getNightLaborAllowance()); // 야간근무수당
        row.createCell(9).setCellValue(severancePayDetail.getHolidayLaborAllowance()); // 휴일근무수당
        row.createCell(10).setCellValue(severancePayDetail.getAnnualAllowance()); // 연차수당
        row.createCell(11).setCellValue(severancePayDetail.getTotalAmount()); // 총합계
        row.createCell(12).setCellValue(severancePayDetail.getStatus().name()); // 지급 상태

    }

    @Transactional(readOnly = true)
    public SeverancePayListResponseDTO getSeverancePays(Pageable pageable) {

        Page<SeverancePay> page = severancePayQueryRepository.findAll(pageable);

        List<SeverancePayResponseDTO> severancePays = page.getContent().stream()
                .map(severancePay -> {
                    // 총 사원 수 (severancePayDetailList의 크기)
                    long totalEmp = severancePay.getSeverancePayDetailList().size();

                    // 총 지급 금액 (severancePayDetailList의 totalAmount 합산)
                    long totalPay = severancePay.getSeverancePayDetailList().stream()
                            .mapToLong(SeverancePayDetail::getTotalAmount) // SeverancePayDetail의 totalAmount 값을 합산
                            .sum();

                    return SeverancePayResponseDTO.builder()
                            .severancePayId(severancePay.getSeverancePayId())
                            .name(severancePay.getName())
                            .createDatetime(LocalDate.from(severancePay.getCreateDatetime()))
                            .totalEmp(totalEmp)
                            .totalPay(totalPay)
                            .build();

                })
                .collect(Collectors.toList());

        return SeverancePayListResponseDTO.builder()
                .severancePays(severancePays)
                .totalPages(page.getTotalPages())
                .totalItems((int) page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();

    }
}
