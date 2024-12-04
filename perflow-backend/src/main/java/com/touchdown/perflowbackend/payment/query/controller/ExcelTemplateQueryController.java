package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.payment.query.service.ExcelTemplateQueryService;
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

    private final ExcelTemplateQueryService excelTemplateQueryService;

    @GetMapping("/payroll-template")
    public ResponseEntity<byte[]> downloadPayrollTemplate() throws IOException {
        byte[] excelData = excelTemplateQueryService.generatePayrollTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "payroll_template.xlsx");

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}