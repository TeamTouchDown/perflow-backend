package com.touchdown.perflowbackend.authority.domain.repository;

import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;

public interface AuthorityEmployeeRepository {
    AuthorityEmployee save(AuthorityEmployee authorityEmployee);
}
