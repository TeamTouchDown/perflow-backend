package com.touchdown.perflowbackend.authority.infrastructure;

import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployeeId;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityEmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorityEmployeeRepository extends JpaRepository<AuthorityEmployee, AuthorityEmployeeId>, AuthorityEmployeeRepository {
}
