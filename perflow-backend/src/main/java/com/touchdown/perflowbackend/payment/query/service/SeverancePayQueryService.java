package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePayDetail;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.query.dto.*;
import com.touchdown.perflowbackend.payment.query.repository.SeverancePayQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        row.createCell(2).setCellValue(employee.getJoinDate()); // 입사일
        row.createCell(3).setCellValue(employee.getResignDate()); // 퇴사일
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

    // 퇴직금 정보 상세 조회
    @Transactional(readOnly = true)
    public SeverancePayDetailResponseDTO getSeverancePay(Long severancePayId) {

        List<Object[]> results = severancePayQueryRepository.findSeverancePayDetails(severancePayId);

        List<SeverancePayDTO> severancePays = mapToDTO(results);  // Object[]를 SeverancePayDTO로 변환

        return SeverancePayDetailResponseDTO.builder()  // SeverancePayDetailResponseDTO로 감쌈
                .severancePays(severancePays)
                .build();

    }

    private List<SeverancePayDTO> mapToDTO(List<Object[]> results) {

        return results.stream()
                .map(result -> new SeverancePayDTO(

                        (Long) result[0],
                        (String) result[1],
                        (String) result[2],
                        toLocalDate(result[3]),
                        toLocalDate(result[4]),
                        (String) result[5],
                        (String) result[6],
                        toLong(result[7]),  // 수정
                        toLong(result[8]),  // 수정
                        toLong(result[9]),  // 수정
                        toLong(result[10]), // 수정
                        toLong(result[11]),
                        Status.valueOf((String) result[12])

                ))

                .collect(Collectors.toList());
    }

    private LocalDate toLocalDate(Object date) {

        if (date == null) {

            return null; // null 처리

        } else if (date instanceof java.sql.Date) {

            return ((java.sql.Date) date).toLocalDate();

        } else if (date instanceof java.util.Date) {

            return ((java.util.Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        } else if (date instanceof LocalDate) {

            return (LocalDate) date;

        } else {

            throw new IllegalArgumentException("Unexpected date type: " + date);

        }
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else {
            throw new IllegalArgumentException("Unexpected type for Long field: " + value.getClass().getName());
        }
    }

    // 퇴직한 사원의 퇴직금 명세서 조회
    @Transactional(readOnly = true)
    public SeverancePayStubDetailDTO getSeverancePayStub(String empId) {
        // 단일 결과 Object[]를 반환받음
        Object[] rawResult = (Object[]) severancePayQueryRepository.findByEmpId(empId);

        // 반환값 디버깅 로그
        log.error("Query result class: {}", rawResult.getClass());
        log.error("Query result contents: {}", Arrays.toString(rawResult));

        // 결과 배열이 null이거나 비어 있는지 확인
        if (rawResult == null || rawResult.length == 0 || !(rawResult[0] instanceof Object[])) {

            throw new IllegalArgumentException("Unexpected result structure or empty result.");

        }

        // 내부 배열 추출
        Object[] result = (Object[]) rawResult[0];

        // 결과가 예상 크기보다 작으면 예외 발생
        if (result.length < 15) {

            throw new IllegalArgumentException("Unexpected result array size: " + result.length);

        }

        // 각 인덱스의 값을 안전하게 캐스팅
        String empIdResult = result[0] instanceof String ? (String) result[0] : null;
        String name = result[1] instanceof String ? (String) result[1] : null;
        LocalDate joinDate = toLocalDate(result[2]);
        LocalDate resignDate = toLocalDate(result[3]);
        String departmentName = result[4] instanceof String ? (String) result[4] : null;
        String positionName = result[5] instanceof String ? (String) result[5] : null;
        Long totalLaborDays = toLong(result[6]);
        Long threeMonthTotalPay = toLong(result[7]);
        Long threeMonthTotalAllowance = toLong(result[8]);
        Long annualAllowance = toLong(result[9]);
        Long totalAllowance = toLong(result[10]);
        Long extendLaborAllowance = toLong(result[11]);
        Long nightLaborAllowance = toLong(result[12]);
        Long holidayLaborAllowance = toLong(result[13]);
        Long totalAmount = toLong(result[14]);

        // DTO 생성
        SeverancePayStubDTO severancePay = new SeverancePayStubDTO(

                empIdResult, name, joinDate, resignDate, departmentName,
                positionName, totalLaborDays, threeMonthTotalPay,
                threeMonthTotalAllowance, annualAllowance, totalAllowance,
                extendLaborAllowance, nightLaborAllowance, holidayLaborAllowance, totalAmount

        );

        log.error("Parsed DTO: {}", severancePay);

        // SeverancePayStubDTO를 SeverancePayStubDetailDTO로 감싸서 반환
        return SeverancePayStubDetailDTO.builder()
                .severancePayStub(severancePay)
                .build();
        
    }
}
