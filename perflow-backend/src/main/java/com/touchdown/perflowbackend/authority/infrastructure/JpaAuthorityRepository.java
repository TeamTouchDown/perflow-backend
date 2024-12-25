package com.touchdown.perflowbackend.authority.infrastructure;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorityRepository extends JpaRepository<Authority, Long>, AuthorityRepository {
}
