package com.touchdown.perflowbackend.authority.domain.repository;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

public interface AuthorityEmployeeRepository {
    AuthorityEmployee save(AuthorityEmployee authorityEmployee);

    AuthorityEmployee findByEmpAndAuthority(Employee emp, Authority authority);

    void delete(AuthorityEmployee authorityEmployee);
}
