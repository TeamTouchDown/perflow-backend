package com.touchdown.perflowbackend.security.util;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUtil {

    private static CustomEmployDetail getAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == null || authentication.getName() == null ||
                !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new CustomException(ErrorCode.SECURITY_CONTEXT_NOT_FOUND);
        }

        return (CustomEmployDetail) authentication.getPrincipal();
    }

    // 로그인 중인 사원의 empId 반환
    public static String getEmpId() {

        CustomEmployDetail detail = getAuthentication();
        return detail.getUsername();
    }
}
