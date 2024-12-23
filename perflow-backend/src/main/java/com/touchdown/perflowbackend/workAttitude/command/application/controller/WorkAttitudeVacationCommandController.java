package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeVacationCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WorkAttitude-Vacation-Controller", description = "휴가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeVacationCommandController {

    private final WorkAttitudeVacationCommandService workAttitudeVacationCommandService;



}
