package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.payment.command.application.service.SeverancePayCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class SeverancePayCommandController {

    private final SeverancePayCommandService severancePayCommandService;

    @PutMapping("/severance-pays/{severancePayId}/update")
    public ResponseEntity<String> updateSeverancePay(

            @PathVariable Long severancePayId,
            @RequestParam("file") MultipartFile file

    ) {

        try {
            // 엑셀 파일 처리 서비스 호출
            severancePayCommandService.updateSeverancePayFromExcel(severancePayId, file);

            // 성공적인 처리 후 응답
            return ResponseEntity.ok(SuccessCode.SEVERANCE_PAY_UPLOAD_SUCCESS.getMessage());

        } catch (IOException e) {
            // 파일 처리 중 오류 발생시
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (CustomException e) {

            throw new CustomException(ErrorCode.EXCEL_TEMPLATE_UPLOAD_ERROR);

        }
    }

}
