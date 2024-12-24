package com.touchdown.perflowbackend.authority.application.controller;

import com.touchdown.perflowbackend.authority.application.dto.AuthorityEmployeeRequestDTO;
import com.touchdown.perflowbackend.authority.application.service.AuthorityEmployeeCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/master/auth")
public class AuthorityEmployeeCommandController {

    private final AuthorityEmployeeCommandService commandService;

    @PostMapping
    public ResponseEntity<SuccessCode> createAuthorityEmployee(
            @RequestBody AuthorityEmployeeRequestDTO authorityEmployeeRequestDTO
    ) {

        commandService.createAuthorityEmployee(authorityEmployeeRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<SuccessCode> deleteAuthorityEmployee(
            @RequestBody AuthorityEmployeeRequestDTO authorityEmployeeRequestDTO
    ) {

        commandService.deleteAuthorityEmployee(authorityEmployeeRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
