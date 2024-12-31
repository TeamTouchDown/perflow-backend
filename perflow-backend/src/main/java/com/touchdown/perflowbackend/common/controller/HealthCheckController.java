package com.touchdown.perflowbackend.common.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<SuccessCode> healthCheck() {
        
        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
