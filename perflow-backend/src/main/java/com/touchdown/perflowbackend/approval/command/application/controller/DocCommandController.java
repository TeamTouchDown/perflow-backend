package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/docs")
public class DocCommandController {

    private final DocCommandRepository docCommandRepository;

}
