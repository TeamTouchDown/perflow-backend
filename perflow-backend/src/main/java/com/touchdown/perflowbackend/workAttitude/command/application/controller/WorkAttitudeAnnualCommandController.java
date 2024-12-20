package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAnnualCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WorkAttitude-Annual-Controller", description = "연차 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class WorkAttitudeAnnualCommandController {
    private final WorkAttitudeAnnualCommandService annualCommandService;

    @Operation(summary = "연차 신청", description = "사원이 연차 신청합니다.")
    @PostMapping("/emp/annual")
    public ResponseEntity<String>namingNecessary(){

        return null;
    }



}