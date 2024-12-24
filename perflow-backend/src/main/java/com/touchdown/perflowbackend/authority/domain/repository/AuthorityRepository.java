package com.touchdown.perflowbackend.authority.domain.repository;

import com.touchdown.perflowbackend.authority.domain.aggregate.AuthType;
import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;

public interface AuthorityRepository {
    Authority findByType(AuthType authType);
}
