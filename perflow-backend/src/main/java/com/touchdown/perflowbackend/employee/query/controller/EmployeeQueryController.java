package com.touchdown.perflowbackend.employee.query.controller;

import com.opencsv.CSVWriter;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeDetailResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeResponseList;
import com.touchdown.perflowbackend.employee.query.service.EmployeeQueryService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeQueryController {

    private final EmployeeQueryService employeeQueryService;

    // 검색한 부서에 속한 회원 조회
    @GetMapping("/employees/depts")
    public ResponseEntity<List<EmployeeQueryResponseDTO>> getEmployees(@RequestParam(name = "departmentId") Long departmentId) {

        return ResponseEntity.ok(employeeQueryService.getDeptEmployees(departmentId));
    }

    // 모든 사원 목록 조회
    @GetMapping("/employees/lists")
    public ResponseEntity<EmployeeResponseList> getEmployees(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ResponseEntity.ok(employeeQueryService.getAllEmployees(pageable));
    }

    @GetMapping("/hr/employees/{empId}") // 관리자의 사원 정보 조회
    public ResponseEntity<EmployeeDetailResponseDTO> getEmployeeDetail(
            @PathVariable(value = "empId") String empId
    ) {

        return ResponseEntity.ok(employeeQueryService.getEmployeeDetail(empId));
    }

    @GetMapping("/employees") // 내 상세정보 조회
    public ResponseEntity<EmployeeDetailResponseDTO> getMyDetail() {

        String empId = EmployeeUtil.getEmpId();

        return ResponseEntity.ok(employeeQueryService.getEmployeeDetail(empId));
    }


    @GetMapping("/employees/lists/search")
    public ResponseEntity<EmployeeResponseList> getEmployeesByName(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "name") String name
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ResponseEntity.ok(employeeQueryService.getAllEmployeesByName(pageable, name));
    }

    @GetMapping("/csv/download")
    public ResponseEntity<byte[]> downloadCsv() {
        // CSV를 메모리에 기록하기 위한 ByteArrayOutputStream
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        // OutputStreamWriter를 통해 UTF-8로 CSVWriter 생성
        try (OutputStreamWriter osw = new OutputStreamWriter(byteOut, StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(osw)) {
            // UTF-8 BOM 추가 (엑셀 등에서 한글 깨짐 방지)
            byteOut.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            String[] header = {
                    "사번",
                    "직위번호",
                    "직책번호",
                    "부서번호",
                    "이름",
                    "성별(Male/Female)",
                    "주민등록번호",
                    "기본급(월급)",
                    "주소",
                    "전화번호",
                    "이메일",
                    "입사일",
            };
            // 헤더 작성
            csvWriter.writeNext(header);
        } catch (Exception e) {
            // 예외 처리 로직
            throw new CustomException(ErrorCode.FAIL_CREAT_FILE);
        }

        // CSV 바이트 배열 추출
        byte[] csvBytes = byteOut.toByteArray();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        // text/csv Content-Type과 UTF-8 인코딩 설정
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        // 파일 다운로드를 위한 Content-Disposition 설정
        headers.setContentDisposition(ContentDisposition.attachment().filename("employee.csv").build());

        // ResponseEntity로 반환
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
