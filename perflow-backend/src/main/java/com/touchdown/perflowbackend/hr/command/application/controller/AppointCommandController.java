package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.Appoint.AppointCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.service.AppointCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppointCommandController {

    private final AppointCommandService appointCommandService;

    @PostMapping("/hr/appoint")
    public ResponseEntity<SuccessCode> createAppoint(
            @RequestBody AppointCreateDTO appointCreateDTO
    ) {

        appointCommandService.createAppoint(appointCreateDTO);

        return ResponseEntity.ok(SuccessCode.APPOINT_CREATE_SUCCESS);
    }
}
